package com.example.demo1;

import com.example.demo1.controller.CommandeController;
import com.example.demo1.controller.ProduitController;
import com.example.demo1.model.Commande;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private ProduitController produitController;
    CommandeController commandeController = new CommandeController();
    private ImageView QRCodeImageView;

    public HelloApplication() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        produitController = new ProduitController();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Création du slider à gauche
        VBox slider = createSlider();
        BorderPane.setMargin(slider, new Insets(10));
        root.setLeft(slider);

        // Contenu principal au centre
        StackPane contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: #ffffff;");
        BorderPane.setMargin(contentPane, new Insets(10));
        root.setCenter(contentPane);

        // Barre de menu en haut
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion de Stocks");
        primaryStage.show();
    }

    // Création du slider à gauche
    private VBox createSlider() {
        VBox slider = new VBox();
        slider.setSpacing(10);
        slider.setStyle("-fx-background-color: #6096BA; -fx-padding: 10;");
        slider.setPrefWidth(200);

        ImageView logoImageView = new ImageView(new Image("C:\\Users\\Aya\\IdeaProjects\\demo1\\src\\main\\resources\\Images\\Logo Optyhealth.png"));
        logoImageView.setFitWidth(200);
        logoImageView.setFitHeight(100);

        Button produitsButton = createButton("Produits");
        produitsButton.setOnAction(event -> {
            VBox produitsView = new VBox(10);
            produitController.afficherProduits(produitsView);
            ((StackPane) ((BorderPane) produitsButton.getScene().getRoot()).getCenter()).getChildren().setAll(produitsView);
        });
        Button commandesButton = createButton("Commandes");
        commandesButton.setOnAction(event -> {
            TableView<Commande> commandeTableView = new TableView<>();
            commandeController.afficherCommandes(commandeTableView);

            Commande selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();
            if (selectedCommande != null) {
                try {
                    // Generate QR code
                    Image qrCodeImage = commandeController.generateQRCode(selectedCommande,QRCodeImageView);
                    if (qrCodeImage != null) {
                        ImageView qrCodeImageView = new ImageView(qrCodeImage);

                        // Create a new BorderPane and set the QR code image as the center content
                        BorderPane rootPane = new BorderPane();
                        rootPane.setCenter(qrCodeImageView);

                        // Set the new BorderPane as the root of the scene
                        Scene scene = commandesButton.getScene();
                        scene.setRoot(rootPane);
                    } else {
                        System.out.println("QR code image is null.");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No commande selected.");
            }
            ((StackPane) ((BorderPane) commandesButton.getScene().getRoot()).getCenter()).getChildren().setAll(commandeTableView);

        });



        Label welcomeLabel = new Label("MARKETPLACE");
        welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 21px;");

        Separator separator = new Separator();

        slider.getChildren().addAll(logoImageView, welcomeLabel, produitsButton,commandesButton, separator);

        return slider;
    }

    // Création de boutons pour le slider
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(179);
        button.setStyle("-fx-background-color: #DFE0E2; -fx-border-color: #DFE0E2; -fx-border-radius: 100; -fx-border-width: 2; -fx-background-radius: 100;");
        return button;
    }

    // Création de la barre de menu en haut
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Info");

        MenuItem quitterItem = new MenuItem("Quitter");
        quitterItem.setOnAction(event -> System.exit(0));

        menu.getItems().addAll(quitterItem);
        menuBar.getMenus().add(menu);

        return menuBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
