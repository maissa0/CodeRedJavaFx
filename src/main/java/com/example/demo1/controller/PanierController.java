package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Commande;
import com.example.demo1.model.Panier;
import com.example.demo1.model.PanierProduit;
import com.example.demo1.model.Produit;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo1.database.DataBase.getConnection;

public class PanierController {
    private Panier panier;
    private TableView<Produit> tableView;
    private Connection connection;
    private TextField  searchField;


    public PanierController() throws SQLException {
        this.panier = new Panier();
        this.tableView = new TableView<>();
        this.connection = DataBase.getConnection();
        this.searchField=new TextField();

    }

    public void ajouterProduitAuPanier(Produit produit) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Ajouter au panier");

        // Création du contenu de la boîte de dialogue
        Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
        dialog.getDialogPane().setContent(spinner);

        // Ajout des boutons OK et Annuler
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Résultat lorsque l'utilisateur clique sur OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return spinner.getValue();
            }
            return null;
        });

        // Affichage de la boîte de dialogue
        dialog.showAndWait().ifPresent(quantite -> {
            if (quantite > 0) {
                double total = quantite * produit.getPrix(); // Calcul du total
                PanierProduit panierProduit = new PanierProduit(produit.getNom(), quantite, produit.getPrix());
                // Mettre à jour le TableView avec les nouveaux éléments du panier
            } else {
                // Afficher un message d'erreur si la quantité est invalide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir une quantité valide (nombre entier positif).");
                alert.showAndWait();
                dialog.close();



            }
        });
    }


    public void afficherProduitsDuPanier() {
        List<PanierProduit> produitsDuPanier = getAllProduitsDuPanier();

        TableView<PanierProduit> tableView = new TableView<>();

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Rechercher un produit");
        searchBar.setOnKeyReleased(event -> {
            String searchTerm = searchBar.getText().toLowerCase();
            tableView.getItems().setAll(produitsDuPanier.stream()
                    .filter(produit -> produit.getNomProduit().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList()));
            search();
        });

        // Table columns
        TableColumn<PanierProduit, String> nomColumn = new TableColumn<>("Nom du produit");
        TableColumn<PanierProduit, Double> prixColumn = new TableColumn<>("Prix");
        TableColumn<PanierProduit, Integer> quantiteColumn = new TableColumn<>("Quantité");
        TableColumn<PanierProduit, Double> totalColumn = new TableColumn<>("Total");
        TableColumn<PanierProduit, Void> payerColumn = new TableColumn<>("");
        TableColumn<PanierProduit, Void> SupprimerColumn = new TableColumn<>("");


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

        // Payer button cell factory (same as before)

        // Add columns to the table
        tableView.getColumns().addAll(nomColumn, prixColumn, quantiteColumn, totalColumn, payerColumn);

        // Add products to the table (filtered initially)
        tableView.getItems().setAll(produitsDuPanier);

        // Inline styles for TableView
        tableView.setStyle("-fx-font-size: 14px; " +
                "-fx-pref-height: 400px; " +
                "-fx-pref-width: 600px; " +
                "-fx-background-color: #e6e6fa; " + // Light blue background
                "-fx-border-color: black; " +  // Blue border
                "-fx-border-width: 1px;");

        // Inline styles for columns
        nomColumn.setStyle("-fx-text-fill: #;"); // Blue text for column headers
        prixColumn.setStyle("-fx-text-fill: #;");
        quantiteColumn.setStyle("-fx-text-fill: #;");
        totalColumn.setStyle("-fx-text-fill: #;");

        // Alternate row coloring using indexProperty
        tableView.setRowFactory(rf -> {
            TableRow<PanierProduit> row = new TableRow<>();
            row.indexProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() % 2 == 0) {
                    row.getStyleClass().add("even");
                } else {
                    row.getStyleClass().removeAll("even");
                }
            });
            row.setOnMouseEntered(event -> row.getStyleClass().add("hover"));
            row.setOnMouseExited(event -> row.getStyleClass().remove("hover"));
            return row;
        });

        // Create a VBox with search bar and table
        VBox vbox = new VBox(searchBar, tableView);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        // Create the scene and show the window
        Stage stage = new Stage();
        stage.setTitle("Panier");
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }




    public List<PanierProduit> getAllProduitsDuPanier() {
        List<PanierProduit> produitsDuPanier = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nom, prix, quantite , date_ajout FROM panier")) {

            while (resultSet.next()) {
                String Nom = resultSet.getString("nom");
                int Prix = resultSet.getInt("prix");
                double quantit = resultSet.getDouble("quantite");
                Date date_ajout = resultSet.getDate("date_ajout");

                PanierProduit produit = new PanierProduit(Nom, Prix, quantit);
                produitsDuPanier.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitsDuPanier;
    }
    private void afficherFormulairePaiement(PanierProduit produit) throws Exception {
        Application paymentForm = new PaymentForm();
        paymentForm.start(new Stage());
    }
    public double calculerTotal(PanierProduit produit) {
        return produit.getPrixUnitaire() * produit.getQuantite();
    }
    private void search() {
        TableView<PanierProduit> tableView = new TableView<>();
        ObservableList<PanierProduit> data = tableView.getItems();
        FilteredList<PanierProduit> filteredData = new FilteredList<>(data, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(obj -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (obj.getNomProduit().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID
                } else if (obj.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches sexe
                } else if (String.valueOf(obj.getPrix()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches age
                }
                // Add other fields to search if needed...

                return false; // Does not match any filter
            });
        });

        SortedList<PanierProduit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

}
