package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Panier;
import com.example.demo1.model.PanierProduit;
import com.example.demo1.model.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo1.database.DataBase.getConnection;

public class PanierController {
    private Panier panier;
    private TableView<Produit> tableView;
    private Connection connection;


    public PanierController() throws SQLException {
        this.panier = new Panier();
        this.tableView = new TableView<>();
        this.connection = DataBase.getConnection();

    }

    // Méthode pour ajouter un produit au panier
    public void ajouterProduitAuPanier(Produit produit) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter au panier");
        dialog.setHeaderText("Quantité du produit");
        dialog.setContentText("Veuillez saisir la quantité du produit:");

        dialog.showAndWait().ifPresent(quantiteStr -> {
            try {
                int quantite = Integer.parseInt(quantiteStr);
                if (quantite > 0) {
                    double total = quantite * produit.getPrix(); // Calcul du total
                    PanierProduit panierProduit = new PanierProduit(produit.getNom(), quantite, produit.getPrix(), total);
                    panier.getItems().add(panierProduit); // Ajout au panier

                    // Afficher un message de confirmation
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Produit ajouté au panier");
                    alert.setHeaderText(null);
                    alert.setContentText("Le produit a été ajouté au panier avec succès.");
                    alert.showAndWait();
                } else {
                    // Afficher un message d'erreur si la quantité est invalide
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez saisir une quantité valide (nombre entier positif).");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Afficher un message d'erreur si la saisie n'est pas un nombre
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un nombre entier pour la quantité.");
                alert.showAndWait();
            }
        });
    }

    public void afficherProduitsDuPanier() {
        List<PanierProduit> produitsDuPanier = getAllProduitsDuPanier();
        TableView<PanierProduit> tableView = new TableView<>();
        TableColumn<PanierProduit, String> nomColumn = new TableColumn<>("Nom du produit");
        TableColumn<PanierProduit, Integer> prixColumn = new TableColumn<>("Prix");
        TableColumn<PanierProduit, Double> quantiteColumn = new TableColumn<>("Quantité");
        TableColumn<PanierProduit, Double> totalColumn = new TableColumn<>("Total");
        TableColumn<PanierProduit, Void> actionColumn = new TableColumn<>("Actions");

        // Associer les propriétés aux colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Ajouter les colonnes à la TableView
        tableView.getColumns().addAll(nomColumn, prixColumn, quantiteColumn, totalColumn);

        // Ajouter les produits du panier à la TableView
        tableView.getItems().addAll(produitsDuPanier);
        Button payerButton = new Button("Payer");
        payerButton.setOnAction(event -> {
            // Ajoutez ici votre logique pour le paiement
            // Par exemple, afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Paiement effectué");
            alert.setHeaderText(null);
            alert.setContentText("Le paiement a été effectué avec succès!");
            alert.showAndWait();
        });

        // Ajouter la table et le bouton à un HBox
        HBox hbox = new HBox(tableView);
        hbox.setSpacing(10);

        // Créer la scène et afficher la fenêtre
        Stage stage = new Stage();
        stage.setTitle("Produits du Panier");
        stage.setScene(new Scene(new VBox(tableView), 600, 400));
        stage.show();
    }

    public List<PanierProduit> getAllProduitsDuPanier() {
        List<PanierProduit> produitsDuPanier = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT Nom, Prix, quantit, Total FROM produitpanier")) {

            while (resultSet.next()) {
                String Nom = resultSet.getString("Nom");
                int Prix = resultSet.getInt("Prix");
                double quantit = resultSet.getDouble("quantit");
                double Total = resultSet.getDouble("Total");

                PanierProduit produit = new PanierProduit(Nom, Prix, quantit, Total);
                produitsDuPanier.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitsDuPanier;
    }

}
