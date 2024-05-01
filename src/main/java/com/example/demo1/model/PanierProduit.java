package com.example.demo1.model;

public class PanierProduit extends Produit {
    private String nomProduit;
    private int quantite;
    private double prixUnitaire;
    private double total;

    public PanierProduit(String nomProduit, int quantite, double prixUnitaire, double total) {
        super();
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.total = total;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PanierProduit{" +
                "nomProduit='" + nomProduit + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", total=" + total +
                '}';
    }

    // Ajoutez ici les getters et setters
    // ...
}

