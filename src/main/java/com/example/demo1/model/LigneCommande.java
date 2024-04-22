package com.example.demo1.model;

public class LigneCommande {
    private Produit produit;
    private int quantite;

    public LigneCommande(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    // Méthode pour calculer le prix total de la ligne de commande
    public double getPrixTotal() {
        return produit.getPrix() * quantite;
    }

    // Autres méthodes (getters, setters, etc.) à ajouter selon les besoins

    // Getter pour récupérer le produit de la ligne de commande
    public Produit getProduit() {
        return produit;
    }

    // Getter pour récupérer la quantité de la ligne de commande
    public int getQuantite() {
        return quantite;
    }
}
