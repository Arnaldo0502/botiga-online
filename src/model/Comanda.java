package model;
import java.sql.Date;

public class Comanda {
    private int id;
    private int clientId;
    private Date data;
    private double total;

    // Constructors
    public Comanda() {}

    public Comanda(int id, int clientId, Date data, double total) {
        this.id = id;
        this.clientId = clientId;
        this.data = data;
        this.total = total;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // Getters and Setters
    
}