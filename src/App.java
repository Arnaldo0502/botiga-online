import util.Connexio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import dao.ProducteDAO;
import model.Producte;

public class App {
    private static Connexio connexio;
    private static ProducteDAO producteDAO;

    public static void main(String[] args) {
        producteDAO = new ProducteDAO();
        Scanner sc = new Scanner(System.in);
        int opcio;
        do {
            System.out.println("===== BOTIGA ONLINE =====");
            System.out.println("1. Gestionar Productes");
            System.out.println("2. Gestionar Clients");
            System.out.println("3. Crear Comanda");
            System.out.println("4. Llistar Comandes");
            System.out.println("0. Sortir");
            System.out.print("Opció: ");
            opcio = sc.nextInt();
            switch (opcio) {
                case 1:
                    menuProducte(sc);
                    break;
                case 2:
                    // Cridar ClientDAO
                    break;
                case 3:
                    // Crear Comanda amb transacció
                    break;
                case 4:
                    // Consultar JOIN
                    break;
            }
        } while (opcio != 0);
        sc.close();
    }

    public static void menuProducte(Scanner sc) {
        int opcio;
        do {
            System.out.println("\n===== GESTIÓ DE PRODUCTES =====");
            System.out.println("1. Inserir Producte");
            System.out.println("2. Llistar Productes");
            System.out.println("3. Actualitzar Producte");
            System.out.println("4. Eliminar Producte");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Opció: ");
            opcio = sc.nextInt();
            try {
                switch (opcio) {
                    case 1:
                        System.out.print("Nom del producte: ");
                        String nom = sc.next();
                        System.out.print("Preu del producte: ");
                        double preu = sc.nextDouble();
                        System.out.print("Estoc del producte: ");
                        int estoc = sc.nextInt();
                        Producte p = new Producte();
                        p.setNom(nom);
                        p.setPreu(preu);
                        p.setEstoc(estoc);
                        producteDAO.inserir(p);
                        System.out.println("Producte inserit correctament.");
                        break;
                    case 2:
                        List<Producte> productes = producteDAO.llistar();
                        for (Producte prod : productes) {
                            System.out.println("ID: " + prod.getId() + ", Nom: " + prod.getNom() +
                                    ", Preu: " + prod.getPreu() + ", Estoc: " + prod.getEstoc());
                        }
                        break;
                    case 3:
                        System.out.print("ID del producte a actualitzar: ");
                        int idAct = sc.nextInt();
                        sc.nextLine(); // consume newline
                        Producte prodActual = producteDAO.obtenirPerId(idAct);
                        if (prodActual == null) {
                            System.out.println("Producte no trobat.");
                            break;
                        }
                        System.out.print("Nou nom del producte (actual: " + prodActual.getNom() + ") [Enter per mantenir]: ");
                        String nouNom = sc.nextLine();
                        if (nouNom.isEmpty()) {
                            nouNom = prodActual.getNom();
                        }
                        System.out.print("Nou preu del producte (actual: " + prodActual.getPreu() + ") [Enter per mantenir]: ");
                        String preuStr = sc.nextLine();
                        double nouPreu = prodActual.getPreu();
                        if (!preuStr.isEmpty()) {
                            try {
                                nouPreu = Double.parseDouble(preuStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Preu no vàlid, mantenint actual.");
                            }
                        }
                        System.out.print("Nou estoc del producte (actual: " + prodActual.getEstoc() + ") [Enter per mantenir]: ");
                        String estocStr = sc.nextLine();
                        int nouEstoc = prodActual.getEstoc();
                        if (!estocStr.isEmpty()) {
                            try {
                                nouEstoc = Integer.parseInt(estocStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Estoc no vàlid, mantenint actual.");
                            }
                        }
                        Producte prodAct = new Producte();
                        prodAct.setId(idAct);
                        prodAct.setNom(nouNom);
                        prodAct.setPreu(nouPreu);
                        prodAct.setEstoc(nouEstoc);
                        producteDAO.actualitzar(prodAct);
                        System.out.println("Producte actualitzat correctament.");
                        break;
                    case 4:
                        System.out.print("ID del producte a eliminar: ");
                        int idElim = sc.nextInt();
                        producteDAO.eliminar(idElim);
                        System.out.println("Producte eliminat correctament.");
                        break;
                    case 0:
                        System.out.println("Tornant al menú principal...");
                        break;
                    default:
                        System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en l'operació: " + e.getMessage());
            }
        } while (opcio != 0);
    }
}
