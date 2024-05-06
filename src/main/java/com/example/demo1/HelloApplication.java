package com.example.demo1;

import com.example.demo1.controller.CommandeController;
import com.example.demo1.controller.ProduitController;
import com.example.demo1.model.Commande;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    public HelloApplication() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("front.fxml"));

        // Créer la scène
        Scene scene = new Scene(root, 800, 600); // Remplacez 800 et 600 par les dimensions souhaitées

        // Définir le titre de la fenêtre
        primaryStage.setTitle("OPTIHEALTH JavaFX");

        // Définir la scène principale
        primaryStage.setScene(scene);

        // Afficher la fenêtre
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
