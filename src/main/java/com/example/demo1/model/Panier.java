package com.example.demo1.model;
import java.util.ArrayList;
import java.util.List;

public class Panier {
    private List<Produit> produits;

    public Panier() {
        this.produits = new ArrayList<>();
    }

    // Méthode pour ajouter un produit au panier
    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }

    // Méthode pour supprimer un produit du panier
    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    // Méthode pour vider le panier
    public void viderPanier() {
        produits.clear();
    }

    // Méthode pour récupérer la liste des produits dans le panier
    public List<Produit> getListeProduits() {
        return produits;
    }

    // Méthode pour calculer le total du panier
    public double calculerTotal() {
        double total = 0;
        for (Produit produit : produits) {
            total += produit.getPrix();
        }
        return total;
    }

    // Ajoutez d'autres méthodes selon les besoins, comme récupérer la quantité d'un produit, etc.
}

