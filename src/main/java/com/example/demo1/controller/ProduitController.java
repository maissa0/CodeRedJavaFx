package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Produit;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javafx.stage.FileChooser;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProduitController {
    private Connection connection;
    private PanierController panierController;
    private VBox produitsView;
    private VBox produitsViewFront;
    private List<Produit> produits;



    public ProduitController() throws SQLException {
        this.connection = DataBase.getConnection();
        this.panierController = new PanierController();

    }

    public void afficherProduits(VBox produitsView) {
        this.produitsView = produitsView;
        List<Produit> produits = getAllProduits();

        produitsView.getChildren().clear();


        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(20);
        tilePane.setVgap(20);

        // Parcourir tous les produits
        for (Produit produit : produits) {
            String nom = produit.getNom();
            String description = produit.getDescription();
            double prix = produit.getPrix();
            String Image = produit.getImage();

            // Créer une carte pour chaque produit
            VBox carteProduit = new VBox(10);
            carteProduit.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px;");
            carteProduit.setMaxWidth(250);

            Image image = new Image("file:" + Image);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250); // Définir la largeur de l'image
            imageView.setFitHeight(150); // Définir la hauteur de l'image
            carteProduit.getChildren().add(imageView);


            // Créer un conteneur pour le nom, la description et le prix
            VBox detailsProduit = new VBox(5);
            detailsProduit.setPadding(new Insets(10));

            // Ajouter le nom du produit
            Label nomLabel = new Label(nom);
            nomLabel.setFont(Font.font(16));
            detailsProduit.getChildren().add(nomLabel);

            // Ajouter la description du produit
            Label descriptionLabel = new Label(description);
            descriptionLabel.setWrapText(true);
            detailsProduit.getChildren().add(descriptionLabel);

            // Ajouter le prix du produit
            Label prixLabel = new Label("Prix: $" + prix);
            detailsProduit.getChildren().add(prixLabel);

            carteProduit.getChildren().add(detailsProduit);

            // Créer un bouton pour convertir le prix en TND

            Button convertButton = new Button("Convertir en TND");
            convertButton.setOnAction(event -> {
                // Convertir le prix en TND en utilisant la fonction de conversion
                double prixEnTND = convertirEnTND(prix);

                // Mettre à jour l'affichage du prix avec le prix converti en TND
                prixLabel.setText("Prix: " + prixEnTND + " TND");
            });
            carteProduit.getChildren().add(convertButton);

            // Créer des boutons pour éditer et supprimer le produit
            HBox boutonsBox = new HBox(10);
            boutonsBox.setAlignment(Pos.CENTER);
            Button editButton = new Button("Editer");
            editButton.setOnAction(event -> {
                // Remplacer les labels par des champs de texte pré-remplis avec les valeurs du produit sélectionné
                TextField editedNomField = new TextField(nom);
                TextField editedDescriptionField = new TextField(description);
                TextField editedPrixField = new TextField(String.valueOf(prix));

                // Remplacer les labels existants par les champs de texte
                detailsProduit.getChildren().clear();
                detailsProduit.getChildren().addAll(
                        new Label("Nom du produit:"), editedNomField,
                        new Label("Description du produit:"), editedDescriptionField,
                        new Label("Prix du produit:"), editedPrixField
                );

                // Gestionnaire d'événements du bouton "Enregistrer"
                Button saveButton = new Button("Enregistrer");
                saveButton.setOnAction(saveEvent -> {
                    // Récupérer les nouvelles valeurs des champs de texte
                    String newNom = editedNomField.getText();
                    String newDescription = editedDescriptionField.getText();
                    double newPrix = Double.parseDouble(editedPrixField.getText());

                    // Appeler la méthode editerProduit du contrôleur avec les nouveaux paramètres
                    editerProduit(nom, newNom, newDescription, newPrix);

                    // Rafraîchir l'affichage des produits
                    afficherProduits(produitsView);
                });
                detailsProduit.getChildren().add(saveButton);
            });

            Button deleteButton = new Button("Supprimer");
            deleteButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Supprimer le produit ?");
                alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Si l'utilisateur clique sur le bouton OK dans la boîte de dialogue
                    String nomProduitASupprimer = produit.getNom(); // Récupérer le nom du produit à supprimer
                    // Appeler la méthode supprimerProduit du contrôleur avec le nom du produit
                    supprimerProduit(nomProduitASupprimer);
                    afficherProduits(produitsView);
                }
            });
            Button addToCartButton = new Button("Ajouter au panier");
            addToCartButton.setOnAction(event -> {
                try {
                    ajouterAuPanier(produit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                panierController.afficherProduitsDuPanier();
            });


            boutonsBox.getChildren().addAll(editButton, deleteButton,addToCartButton);
            carteProduit.getChildren().add(boutonsBox);

            // Ajouter la carte au TilePane
            tilePane.getChildren().add(carteProduit);
        }

        // Ajouter le TilePane contenant toutes les cartes de produits à la vue des produits
        produitsView.getChildren().add(tilePane);
        Button ajouterButton = new Button("Ajouter un produit");
        ajouterButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 3px;");
        ajouterButton.setOnAction(event -> {
            afficherFormulaireAjoutProduit(produitsView);
        });


        // Créer une VBox pour contenir la TilePane et le bouton d'ajout de produit
        VBox container = new VBox();
        container.getChildren().addAll(tilePane, ajouterButton);
        VBox.setVgrow(tilePane, Priority.ALWAYS);

        // Ajouter le conteneur à la vue des produits
        produitsView.getChildren().add(container);
    }
    public void afficherFormulaireAjoutProduit(VBox produitsView) {
        produitsView.getChildren().clear(); // Effacer le contenu précédent de produitsView

        // Créer les champs pour saisir les informations du produit
        TextField nomField = new TextField();
        nomField.setPromptText("Nom du produit");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description du produit");

        TextField prixField = new TextField();
        prixField.setPromptText("Prix du produit");
// Créer un ImageView pour afficher l'image sélectionnée
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250); // Définir la largeur de l'image
        imageView.setFitHeight(150); // Définir la hauteur de l'image

        // Bouton pour choisir une image
        String[] imagePath = new String[1]; // Utilisez un tableau pour stocker le chemin de l'image
        imagePath[0] = ""; // Initialisation du chemin de l'image avec une chaîne vide

        Button choisirImageButton = new Button("Choisir une image");
         // Déplacez la déclaration de l'ImageView à l'extérieur pour qu'elle soit accessible

        choisirImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");

            // Filtre pour ne montrer que les fichiers image
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                    "Fichiers d'images", "*.jpg", "*.jpeg", "*.png", "*.gif");
            fileChooser.getExtensionFilters().add(imageFilter);

            // Afficher la boîte de dialogue de sélection de fichier
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                // Charger l'image sélectionnée dans l'ImageView
                imagePath[0] = selectedFile.toURI().toString();
                Image image = new Image(imagePath[0]);
                imageView.setImage(image); // Afficher l'image dans l'ImageView
            }
        });

        Button ajouterProduitButton = new Button("Ajouter le produit");
        ajouterProduitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 3px;");
        ajouterProduitButton.setOnAction(event -> {
            // Récupérer les valeurs saisies dans les champs
            String produitNom = nomField.getText();
            String produitDescription = descriptionField.getText();
            String produitPrixText = prixField.getText().trim();

            // Vérifier si les champs sont remplis correctement
            if (produitNom.isEmpty() || produitDescription.isEmpty() || produitPrixText.isEmpty()) {
                // Afficher un message d'erreur si un champ est vide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return; // Arrêter le traitement si un champ est vide
            }

            // Vérifier si le produit existe déjà dans la base de données
            if (produitExiste(produitNom)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le produit existe déjà.");
                alert.showAndWait();
                return; // Arrêter le traitement si le produit existe déjà
            }

            double produitPrix;
            try {
                produitPrix = Double.parseDouble(produitPrixText);
            } catch (NumberFormatException e) {
                // Afficher un message d'erreur si le prix n'est pas valide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir un prix valide.");
                alert.showAndWait();
                return;
            }

            ajouterProduit(produitNom, produitDescription, produitPrix, imagePath[0]); // Utilisez imagePath[0] pour obtenir le chemin de l'image
            afficherProduits(produitsView);
            nomField.clear();
            descriptionField.clear();
            prixField.clear();
            imageView.setImage(null); // Réinitialiser l'image affichée dans l'ImageView
        });

        VBox formulaireAjoutProduit = new VBox(10);
        formulaireAjoutProduit.getChildren().addAll(nomField, descriptionField, prixField, choisirImageButton,imageView ,  ajouterProduitButton);
        produitsView.getChildren().add(formulaireAjoutProduit);
    }
    public void afficherProduitsFront(VBox produitsViewFront) {
        this.produitsViewFront = produitsViewFront;
        List<Produit> produits = getAllProduits();
        produitsViewFront.getChildren().clear();


        HBox galleryBox = new HBox();
        galleryBox.setPadding(new Insets(80));
        galleryBox.setSpacing(30);

        // Parcourir tous les produits
        for (Produit produit : produits) {
            String nom = produit.getNom();
            String description = produit.getDescription();
            double prix = produit.getPrix();
            String Image = produit.getImage();

            // Créer une carte pour chaque produit
            VBox carteProduit = new VBox(10);
            carteProduit.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px;");
            carteProduit.setMaxWidth(250);

            Image image = new Image("file:" + Image);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250); // Définir la largeur de l'image
            imageView.setFitHeight(150); // Définir la hauteur de l'image
            carteProduit.getChildren().add(imageView);


            // Créer un conteneur pour le nom, la description et le prix
            VBox detailsProduit = new VBox(5);
            detailsProduit.setPadding(new Insets(10));

            // Ajouter le nom du produit
            Label nomLabel = new Label(nom);
            nomLabel.setFont(Font.font(16));
            detailsProduit.getChildren().add(nomLabel);

            // Ajouter la description du produit
            Label descriptionLabel = new Label(description);
            descriptionLabel.setWrapText(true);
            detailsProduit.getChildren().add(descriptionLabel);

            // Ajouter le prix du produit
            Label prixLabel = new Label("Prix: $" + prix);
            detailsProduit.getChildren().add(prixLabel);

            carteProduit.getChildren().add(detailsProduit);

            // Créer un bouton pour convertir le prix en TND

            Button convertButton = new Button("Convertir en TND");
            convertButton.setOnAction(event -> {
                // Convertir le prix en TND en utilisant la fonction de conversion
                double prixEnTND = convertirEnTND(prix);

                // Mettre à jour l'affichage du prix avec le prix converti en TND
                prixLabel.setText("Prix: " + prixEnTND + " TND");
            });
            Button addToCartButton = new Button("Ajouter au panier");
            addToCartButton.setOnAction(event -> {
                try {
                    ajouterAuPanier(produit);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                panierController.afficherProduitsDuPanier();
            });

            carteProduit.getChildren().addAll(convertButton,addToCartButton);

            // Ajouter la carte au TilePane
            galleryBox.getChildren().add(carteProduit);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(galleryBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Afficher toujours la barre de défilement horizontale
        scrollPane.setFitToWidth(true);

        // Ajouter le conteneur à la vue des produits
        produitsViewFront.getChildren().add(scrollPane);
    }



    public List<Produit> getAllProduits() {

        List<Produit> produits = new ArrayList<>();

        try (Connection connection = DataBase.getConnection()) {
            String query = "SELECT nom_produit, description, prix , image FROM produit";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nom = resultSet.getString("nom_produit");
                String description = resultSet.getString("description");
                double prix = resultSet.getDouble("prix");
                String image =resultSet.getString("image");

                Produit produit = new Produit(nom, description, prix,image);
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void ajouterProduit(String nom, String description, double prix, String imagePath ) {
        Produit produit = new Produit(nom, description, prix, imagePath);
        try {
            String query = "INSERT INTO produit (nom_produit, description, prix , image ) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, description);
            statement.setDouble(3, prix);
            statement.setString(4,imagePath);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void supprimerProduit(String nom) {
        try {
            String query = "DELETE FROM produit WHERE nom_produit = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nom);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour éditer un produit
    public void editerProduit(String ancienNom, String nouveauNom, String description, double prix) {
        try {
            String query = "UPDATE produit SET nom_produit = ?, description = ?, prix = ? WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nouveauNom);
            statement.setString(2, description);
            statement.setDouble(3, prix);
            statement.setString(4, ancienNom);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double convertirEnTND(double prixEnDollar) {
        // Supposons que 1 dollar équivaut à 2 dinars tunisiens
        double tauxDeChange = 3.0;

        // Calculer le prix en TND
        double prixEnTND = prixEnDollar * tauxDeChange;

        return prixEnTND;
    }
    public boolean produitExiste(String nomProduit) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Établir la connexion à la base de données
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/optihealth", "root", "");

            // Requête pour vérifier si un produit avec le même nom existe
            String query = "SELECT COUNT(*) FROM produit WHERE nom_produit = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, nomProduit);
            resultSet = statement.executeQuery();

            // Si un produit avec le même nom existe, retourner vrai
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources JDBC
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Si aucun produit avec le même nom n'a été trouvé, retourner faux
        return false;
    }
    public void ajouterAuPanier(Produit produit) throws SQLException {
        panierController.ajouterProduitAuPanier(produit);
    }


}
