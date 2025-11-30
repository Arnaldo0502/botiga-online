package dao;

import java.sql.*;
import util.Connexio;
import model.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void inserir(Client p) throws SQLException {
        String sql = "INSERT INTO Clients (nom, correu) VALUES (?, ?)";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<Client> llistar() throws SQLException {
        List<Client> Clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client p = new Client();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("nom"));
                p.setEmail(rs.getString("correu"));
                Clients.add(p);
            }
        }
        return Clients;
    }

    public void actualitzar(Client p) throws SQLException {
        String sql = "UPDATE Clients SET nom=?, correu=? WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getEmail());
            stmt.setInt(3, p.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Clients WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Client obtenirPerId(int id) throws SQLException {
        String sql = "SELECT * FROM Clients WHERE id=?";
        try (Connection conn = Connexio.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client p = new Client();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("nom"));
                    p.setEmail(rs.getString("correu"));
                    return p;
                }
            }
        }
        return null;
    }
}
