package com.example.demo1.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Panier {

    private Map<Produit, Integer> produits;

    public Panier() {
        produits = new HashMap<>();
    }

    public void ajouterProduit(Produit produit, int quantite) {
        produits.put(produit, quantite);
    }

    public List<PanierProduit> getItems() {
        List<PanierProduit> items = new ArrayList<>();
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            Produit produit = entry.getKey();
            int quantite = entry.getValue();
            double prixUnitaire = produit.getPrix(); // Supposons que getPrix() renvoie le prix unitaire du produit
            double total = quantite * prixUnitaire;
            PanierProduit item = new PanierProduit(produit.getNom(), quantite, prixUnitaire );
            items.add(item);
        }
        return items;
    }


}

