package dao;

import util.Connexio;

import java.sql.*;

public class ConsultesDAO {

    public void llistarComandesClient(int clientId) throws SQLException {
        String sql = "SELECT c.id as comanda_id, c.data, c.total, lc.quantitat, lc.preuUnitari, p.nom as producte_nom " +
                "FROM Comandes c " +
                "JOIN LiniesComanda lc ON c.id = lc.comanda_id " +
                "JOIN Productes p ON lc.producte_id = p.id " +
                "WHERE c.client_id = ? " +
                "ORDER BY c.id, lc.id";

        try (Connection conn = Connexio.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                int currentComandaId = -1;
                while (rs.next()) {
                    int comandaId = rs.getInt("comanda_id");
                    if (comandaId != currentComandaId) {
                        if (currentComandaId != -1) {
                            System.out.println();
                        }
                        System.out.println("Comanda ID: " + comandaId + ", Data: " + rs.getDate("data") + ", Total: " + rs.getDouble("total"));
                        currentComandaId = comandaId;
                    }
                    System.out.println("  - Producte: " + rs.getString("producte_nom") + ", Quantitat: " + rs.getInt("quantitat") + ", Preu Unitari: " + rs.getDouble("preuUnitari"));
                }
                if (currentComandaId == -1) {
                    System.out.println("No s'han trobat comandes per aquest client.");
                }
            }
        }
    }

    public void mostrarTotalsAmbDescomptes() throws SQLException {
        String sql = "SELECT c.id, c.client_id, c.data, c.total FROM Comandes c ORDER BY c.id";
        try (Connection conn = Connexio.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int comandaId = rs.getInt("id");
                double total = rs.getDouble("total");
                double descompte = calcularDescompte(conn, comandaId);
                System.out.println("Comanda ID: " + comandaId + ", Client ID: " + rs.getInt("client_id") + ", Data: " + rs.getDate("data") + ", Total Final: " + total + ", Descompte Aplicat: " + descompte);
            }
        }
    }

    private double calcularDescompte(Connection conn, int comandaId) throws SQLException {
        double totalDescompte = 0;
        String sql = "SELECT lc.quantitat, lc.preuUnitari, d.tipus, d.valor " +
                "FROM LiniesComanda lc " +
                "JOIN Descomptes d ON lc.producte_id = d.producte_id " +
                "WHERE lc.comanda_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comandaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int quantitat = rs.getInt("quantitat");
                    double preuUnitari = rs.getDouble("preuUnitari");
                    String tipus = rs.getString("tipus");
                    double valor = rs.getDouble("valor");
                    double subtotal = quantitat * preuUnitari;
                    double descompte = 0;
                    if ("%".equals(tipus)) {
                        descompte = subtotal * (valor / 100.0);
                    } else if ("€".equals(tipus)) {
                        descompte = valor * quantitat;
                    }
                    totalDescompte += descompte;
                }
            }
        }
        return totalDescompte;
    }
}
