package com.example.demo1;

import com.example.demo1.controller.CommandeController;
import com.example.demo1.controller.ProduitController;
import com.example.demo1.model.Commande;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private ProduitController produitController;
    CommandeController commandeController = new CommandeController();

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
        slider.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10;");
        slider.setPrefWidth(200);

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
            ((StackPane) ((BorderPane) commandesButton.getScene().getRoot()).getCenter()).getChildren().setAll(commandeTableView);
        });

        slider.getChildren().addAll(produitsButton, commandesButton);


        return slider;
    }

    // Création de boutons pour le slider
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(180);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 30;");
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