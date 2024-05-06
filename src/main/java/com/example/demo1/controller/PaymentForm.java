package com.example.demo1.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class PaymentForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création d'un GridPane pour organiser les éléments du formulaire de paiement
        GridPane formLayout = new GridPane();
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setHgap(10);
        formLayout.setVgap(10);
        formLayout.setPadding(new Insets(20));

        // Ajout des labels et champs de saisie pour les informations de la carte
        Label cardNumberLabel = new Label("Numéro de carte:");
        TextField cardNumberField = new TextField();
        cardNumberField.setMaxWidth(200);

        Label expirationDateLabel = new Label("Date d'expiration:");
        DatePicker expirationDatePicker = new DatePicker();

        Label securityCodeLabel = new Label("Code de sécurité:");
        TextField securityCodeField = new TextField();
        securityCodeField.setMaxWidth(50);

        // Ajout des labels et champs de saisie pour les informations de facturation
        Label nameLabel = new Label("Nom complet:");
        TextField nameField = new TextField();
        nameField.setMaxWidth(200);

        Label addressLabel = new Label("Adresse:");
        TextField addressField = new TextField();
        addressField.setMaxWidth(200);

        Label cityLabel = new Label("Ville:");
        TextField cityField = new TextField();
        cityField.setMaxWidth(150);

        Label countryLabel = new Label("Pays:");
        ComboBox<String> countryComboBox = new ComboBox<>();
        countryComboBox.getItems().addAll(
                "France",
                "Belgique",
                "Suisse",
               "Tunisie",
                "Allemagne",
                "Italie",
                "Espagne",
                "Royaume-Uni",
                "États-Unis",
                "Canada",
                "Egypt "
                // et ainsi de suite...
        );
        Label postalCodeLabel = new Label("Code postal:");
        TextField postalCodeField = new TextField();
        postalCodeField.setMaxWidth(50);

        // Ajout des éléments au GridPane
        formLayout.add(cardNumberLabel, 0, 0);
        formLayout.add(cardNumberField, 1, 0);
        formLayout.add(expirationDateLabel, 0, 1);
        formLayout.add(expirationDatePicker, 1, 1);
        formLayout.add(securityCodeLabel, 2, 1);
        formLayout.add(securityCodeField, 3, 1);

        formLayout.add(nameLabel, 0, 2);
        formLayout.add(nameField, 1, 2);
        formLayout.add(addressLabel, 0, 3);
        formLayout.add(addressField, 1, 3);
        formLayout.add(cityLabel, 2, 3);
        formLayout.add(cityField, 3, 3);
        formLayout.add(countryLabel, 0, 4);
        formLayout.add(countryComboBox, 1, 4);
        formLayout.add(postalCodeLabel, 2, 4);
        formLayout.add(postalCodeField, 3, 4);
        Button submitButton = new Button("Valider");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Traiter le paiement ici (validation des informations, envoi à un serveur de paiement, etc.)
                System.out.println("Paiement en cours...");
                PaymentController PaymentController = null;
                PaymentController = new PaymentController();

                // Appeler la méthode processPayment() de PanierController pour effectuer le paiement
                PaymentController.processPayment();
                // Fermer la fenêtre modale après le traitement du paiement
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            }
        });

        // Ajout du bouton au GridPane
        formLayout.add(submitButton, 2, 5);

        // Création de la scène pour le formulaire de paiement
        Scene paymentScene = new Scene(formLayout, 800, 600);

        // Création de la fenêtre modale pour le formulaire de paiement
        Stage paymentStage = new Stage();
        paymentStage.initModality(Modality.APPLICATION_MODAL);
        paymentStage.setTitle("Formulaire de Paiement");
        paymentStage.setScene(paymentScene);

        // Affichage de la fenêtre modale pour le formulaire de paiement
        paymentStage.show();

        // Définir la taille de la fenêtre modale pour qu'elle ne puisse pas être redimensionnée
        paymentStage.setResizable(false);
    }
    }


