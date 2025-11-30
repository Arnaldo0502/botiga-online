package dao;

import util.Connexio;
import model.Comanda;
import model.LiniaComanda;

import java.sql.*;

public class ComandaDAO {
    public void crearComanda(Comanda comanda) throws SQLException {
        Connection connexio = null;
        try {
            connexio = Connexio.getConnection();
            connexio.setAutoCommit(false);

            // comprovar y decrementar estoc
            String comprovarEstocSQL = "SELECT estoc FROM Productes WHERE id = ?";
            String decrementarEstocSQL = "UPDATE Productes SET estoc = estoc - ? WHERE id = ?";

            PreparedStatement psSelect = connexio.prepareStatement(comprovarEstocSQL);
            PreparedStatement psUpdate = connexio.prepareStatement(decrementarEstocSQL);

            for (LiniaComanda linia : comanda.getLinies()) {
                // Comprovar estoc
                psSelect.setInt(1, linia.getProducteId());
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    int estocActual = rs.getInt("estoc");
                    if (estocActual < linia.getQuantitat()) {
                        throw new SQLException("Estoc insuficient per al producte ID: " + linia.getProducteId());
                    }
                } else {
                    throw new SQLException("Producte no trobat ID: " + linia.getProducteId());
                }
                rs.close();

                // Decrementar estoc
                psUpdate.setInt(1, linia.getQuantitat());
                psUpdate.setInt(2, linia.getProducteId());
                psUpdate.executeUpdate();
            }

            // Insertar comanda
            String inserirComandaSQL = "INSERT INTO Comandes (client_id, data, total) VALUES (?, ?, ?)";
            PreparedStatement psComanda = connexio.prepareStatement(inserirComandaSQL, Statement.RETURN_GENERATED_KEYS);
            psComanda.setInt(1, comanda.getClientId());
            psComanda.setDate(2, comanda.getData());
            psComanda.setDouble(3, comanda.getTotal());
            psComanda.executeUpdate();

            // Obtenir l'ID generat de la comanda inserida i insertar línies
            ResultSet generatedKeys = psComanda.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("No s'ha pogut generar ID de comanda.");
            }
            int comandaId = generatedKeys.getInt(1);

            // Insertar línies de comanda
            String sqlInsertLinia = "INSERT INTO LiniesComanda (comanda_id, producte_id, quantitat, preuUnitari) VALUES (?, ?, ?, ?)";
            PreparedStatement psLinia = connexio.prepareStatement(sqlInsertLinia);

            double total = 0.0;

            for (LiniaComanda linia : comanda.getLinies()) {
                psLinia.setInt(1, comandaId);
                psLinia.setInt(2, linia.getProducteId());
                psLinia.setInt(3, linia.getQuantitat());
                psLinia.setDouble(4, linia.getPreuUnitari());
                psLinia.executeUpdate();

                total += linia.getQuantitat() * linia.getPreuUnitari();
            }
            Savepoint sp = connexio.setSavepoint("DespresLinies");
            // Intentar aplicar descomptes
            try {
                
                total -= aplicarDescomptes(connexio, comandaId);

            } catch (Exception e) {
                System.out.println("Error aplicant descomptes: " + e.getMessage());
                connexio.rollback(sp); 
            }

            // actualitzar total de la comanda
            String actualitzarTotalSQL = "UPDATE Comandes SET total = ? WHERE id = ?";
            PreparedStatement psActualitzarTotal = connexio.prepareStatement(actualitzarTotalSQL);
            psActualitzarTotal.setDouble(1, comanda.getTotal());
            psActualitzarTotal.setInt(2, comanda.getId());
            psActualitzarTotal.executeUpdate();

            connexio.commit();
            System.out.println("Comanda creada correctament - ID: " + comandaId);

        } catch (SQLException e) {
            if (connexio != null) {
                connexio.rollback();
            }
            System.out.println("Error: " + e.getMessage());
            throw e;

        } finally {
            if (connexio != null)
                connexio.setAutoCommit(true);
        }
    }

    private double aplicarDescomptes(Connection conn, int comandaId) throws SQLException {
        double totalDescompte = 0;

        String sql = "SELECT lc.quantitat, lc.preuUnitari, d.tipus, d.valor " +
                "FROM LiniesComanda lc " +
                "JOIN Descomptes d ON lc.producte_id = d.producte_id " +
                "WHERE lc.comanda_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, comandaId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int quantitat = rs.getInt("quantitat");
            double preuUnitari = rs.getDouble("preuUnitari");
            String tipus = rs.getString("tipus");
            double valor = rs.getDouble("valor");

            double subtotal = quantitat * preuUnitari;
            double descompte = 0;

            if (tipus.equals("%")) {
                descompte = subtotal * (valor / 100.0);
            } else if (tipus.equals("€")) {
                descompte = valor * quantitat;
            }

            totalDescompte += descompte;
        }

        System.out.println("Descompte aplicat: -" + totalDescompte + " €");
        return totalDescompte;
    }

}