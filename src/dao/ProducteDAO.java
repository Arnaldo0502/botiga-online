package dao;

import java.sql.*;
import util.Connexio;
import model.Producte;
import java.util.ArrayList;
import java.util.List;

public class ProducteDAO {

    public void inserir(Producte p) throws SQLException {
        String sql = "INSERT INTO Productes (nom, preu, estoc) VALUES (?, ?, ?)";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNom());
            stmt.setDouble(2, p.getPreu());
            stmt.setInt(3, p.getEstoc());
            stmt.executeUpdate();
        }
    }

    public List<Producte> llistar() throws SQLException {
        List<Producte> productes = new ArrayList<>();
        String sql = "SELECT * FROM Productes";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Producte p = new Producte();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPreu(rs.getDouble("preu"));
                p.setEstoc(rs.getInt("estoc"));
                productes.add(p);
            }
        }
        return productes;
    }

    public void actualitzar(Producte p) throws SQLException {
        String sql = "UPDATE Productes SET nom=?, preu=?, estoc=? WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNom());
            stmt.setDouble(2, p.getPreu());
            stmt.setInt(3, p.getEstoc());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Productes WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Producte obtenirPerId(int id) throws SQLException {
        String sql = "SELECT * FROM Productes WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producte p = new Producte();
                    p.setId(rs.getInt("id"));
                    p.setNom(rs.getString("nom"));
                    p.setPreu(rs.getDouble("preu"));
                    p.setEstoc(rs.getInt("estoc"));
                    return p;
                }
            }
        }
        return null;
    }
}
