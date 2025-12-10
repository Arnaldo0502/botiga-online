package dao;

import util.Connexio;
import model.Comanda;
import model.LiniaComanda;

import java.sql.*;
import java.util.Scanner;

public class ComandaDAO {
    private DescompteDAO descompteDAO = new DescompteDAO();

    public void crearComanda(Comanda comanda) throws SQLException {
        Connection connexio = null;
        Scanner scanner = new Scanner(System.in);
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

            Savepoint sp;
            System.out.println("Vols aplicar descomptes a la comanda? (s/n)");

            char resposta;

            while (true) {
                String entrada = scanner.nextLine().trim();

                // evitar vacío
                if (entrada.isEmpty()) {
                    System.out.println("Entrada buida. Torna-ho a intentar: (s/n)");
                    continue;
                }
                resposta = Character.toLowerCase(entrada.charAt(0));
                // validar respuesta
                if (resposta == 's' || resposta == 'n') {
                    break;
                }
                System.out.println("Opció no vàlida. Introdueix 's' o 'n'.");
            }

            // si es 's', aplicar descuentos
            if (resposta == 's') {
                sp = connexio.setSavepoint("AbansDescomptes");

                try {
                    total -= descompteDAO.aplicarDescomptes(connexio, comandaId);
                } catch (Exception e) {
                    System.out.println("Error aplicant descomptes: " + e.getMessage());
                    connexio.rollback(sp);
                }
            }

            // actualitzar total de la comanda
            String actualitzarTotalSQL = "UPDATE Comandes SET total = ? WHERE id = ?";
            PreparedStatement psActualitzarTotal = connexio.prepareStatement(actualitzarTotalSQL);
            psActualitzarTotal.setDouble(1, total); // Usar la variable total actualizada
            psActualitzarTotal.setInt(2, comandaId); // Usar comandaId en lugar de comanda.getId()
            psActualitzarTotal.executeUpdate();

            connexio.commit();
            System.out.println("Comanda creada correctament - ID: " + comandaId);

        } catch (SQLException e) {
            if (connexio != null) {
                connexio.rollback();
            }
            connexio.rollback();
            System.out.println("Error: " + e.getMessage());

        } finally {
            if (connexio != null)
                connexio.setAutoCommit(true);
        }
    }

}