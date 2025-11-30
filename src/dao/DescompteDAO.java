package dao;

import java.sql.*;
import util.Connexio;
import model.Descompte;
import java.util.ArrayList;
import java.util.List;

public class DescompteDAO {

    public void inserir(Descompte d) throws SQLException {
        String sql = "INSERT INTO Descomptes (producte_id, tipus, valor) VALUES (?, ?, ?)";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getProducteId());
            stmt.setString(2, d.getTipus());
            stmt.setDouble(3, d.getValor());
            stmt.executeUpdate();
        }
    }

    public List<Descompte> llistar() throws SQLException {
        List<Descompte> descomptes = new ArrayList<>();
        String sql = "SELECT * FROM Descomptes";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Descompte d = new Descompte();
                d.setId(rs.getInt("id"));
                d.setProducteId(rs.getInt("producte_id"));
                d.setTipus(rs.getString("tipus"));
                d.setValor(rs.getDouble("valor"));
                descomptes.add(d);
            }
        }
        return descomptes;
    }

    public void actualitzar(Descompte d) throws SQLException {
        String sql = "UPDATE Descomptes SET producte_id=?, tipus=?, valor=? WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getProducteId());
            stmt.setString(2, d.getTipus());
            stmt.setDouble(3, d.getValor());
            stmt.setInt(4, d.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Descomptes WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Descompte obtenirPerId(int id) throws SQLException {
        String sql = "SELECT * FROM Descomptes WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Descompte d = new Descompte();
                    d.setId(rs.getInt("id"));
                    d.setProducteId(rs.getInt("producte_id"));
                    d.setTipus(rs.getString("tipus"));
                    d.setValor(rs.getDouble("valor"));
                    return d;
                }
            }
        }
        return null;
    }

    public double aplicarDescomptes(Connection conn, int comandaId) throws SQLException {
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
