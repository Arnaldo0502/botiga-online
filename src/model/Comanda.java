package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Comanda {
    private int id;
    private int clientId;
    private Date data;
    private double total;
    private List<LiniaComanda> linies;

    // Constructors
    public Comanda() {
        linies = new ArrayList<>();
    }

    public Comanda(int id, int clientId, Date data, double total) {
        this.id = id;
        this.clientId = clientId;
        this.data = data;
        this.total = total;
        this.linies = new ArrayList<>();
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

    public List<LiniaComanda> getLinies() {
        return linies;
    }

    public void setLinies(List<LiniaComanda> linies) {
        this.linies = linies;
    }

    public void addLinia(LiniaComanda linia) {
        this.linies.add(linia);
    }

}