package com.example.demo1.model;

public class PanierProduit extends Produit {
    private String nomProduit;
    private int quantite;
    private double prixUnitaire;

    public PanierProduit(String nomProduit, int quantite, double prixUnitaire) {
        super();
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
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



    @Override
    public String toString() {
        return "PanierProduit{" +
                "nomProduit='" + nomProduit + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }

    // Ajoutez ici les getters et setters
    // ...
}

