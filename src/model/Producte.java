package model;
public class Producte {
    private int id;
    private String nom;
    private double preu;
    private int estoc;
    // Constructors
    public Producte() {}
    public Producte(int id, String nom, double preu, int estoc) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.estoc = estoc;
    }
    // Getters i setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getPreu() { return preu; }
    public void setPreu(double preu) { this.preu = preu; }
    public int getEstoc() { return estoc; }
    public void setEstoc(int estoc) { this.estoc = estoc; }
}
