package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Panier;
import com.example.demo1.model.PanierProduit;
import com.example.demo1.model.Produit;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
                    PanierProduit panierProduit = new PanierProduit(produit.getNom(), quantite, produit.getPrix());
                    panier.getItems().add(panierProduit); // Ajout au panier
                    // Rest of your code
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
        TableColumn<PanierProduit, Double> prixColumn = new TableColumn<>("Prix");
        TableColumn<PanierProduit, Integer> quantiteColumn = new TableColumn<>("Quantité");
        TableColumn<PanierProduit, Double> totalColumn = new TableColumn<>("Total");
        TableColumn<PanierProduit, Void> payerColumn = new TableColumn<>("");

        // Créer une cellule de type bouton pour la colonne Payer
        payerColumn.setCellFactory(param -> new TableCell<PanierProduit, Void>() {
            private final Button payerButton = new Button("Payer");

            {
                // Définir l'action du bouton Payer pour chaque ligne
                payerButton.setOnAction(event -> {
                    PanierProduit produit = getTableView().getItems().get(getIndex());
                    try {
                        afficherFormulairePaiement(produit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Paiement pour le produit : " + produit.getNomProduit());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(payerButton);
                }
            }
        });

        // Set cell value factories
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Calculate total for each product
        totalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(calculerTotal(cellData.getValue())).asObject());

        // Add columns to the table
        tableView.getColumns().addAll(nomColumn, prixColumn, quantiteColumn, totalColumn,payerColumn);

        // Add products to the table
        tableView.getItems().addAll(produitsDuPanier);

        // CSS for TableView
        String tableViewStyle ="-fx-font-size: 14px; " +
                "-fx-pref-height: 400px; " +
                "-fx-pref-width: 600px; " +
                "-fx-background-color: #ccffcc; " + // Light green background
                "-fx-border-color: #000000; " + // Black border
                "-fx-border-width: 1px;";

        // Apply the style to the TableView
        tableView.setStyle(tableViewStyle);


        // Style personnalisé pour le bouton Payer
        // Créer un conteneur VBox pour la TableView et le bouton
        VBox vbox = new VBox(tableView);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        // Créer la scène et afficher la fenêtre
        Stage stage = new Stage();
        stage.setTitle("Produits du Panier");
        stage.setScene(new Scene(new VBox(vbox), 600, 400));
        stage.show();
    }


    public List<PanierProduit> getAllProduitsDuPanier() {
        List<PanierProduit> produitsDuPanier = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT Nom, Prix, quantit FROM produitpanier")) {

            while (resultSet.next()) {
                String Nom = resultSet.getString("Nom");
                int Prix = resultSet.getInt("Prix");
                double quantit = resultSet.getDouble("quantit");

                PanierProduit produit = new PanierProduit(Nom, Prix, quantit);
                produitsDuPanier.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitsDuPanier;
    }
    private void afficherFormulairePaiement(PanierProduit produit) throws Exception {
        // Créer une nouvelle instance de PaymentForm
        Application paymentForm = new PaymentForm();

        // Appeler la méthode start() de PaymentForm pour afficher le formulaire de paiement
        paymentForm.start(new Stage());
    }
    public double calculerTotal(PanierProduit produit) {
        return produit.getPrixUnitaire() * produit.getQuantite();
    }

}
