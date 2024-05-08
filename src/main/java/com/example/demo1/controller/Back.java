package com.example.demo1.controller;

import com.example.demo1.model.Commande;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Back {

@FXML
    private VBox produitsView;
@FXML
    private Stage stage;
    private ProduitController produitController;
    private CommandeController commandeController;
    TableView<Commande> tableView = new TableView<>();


    public void initialize() throws SQLException {
        produitsView = new VBox();
        produitController = new ProduitController();
        commandeController = new CommandeController();
        stage=new Stage();

    }

    @FXML
    private void handleProduitsBButtonClick() {
        produitController.afficherProduits(produitsView);
        Scene scene = new Scene(produitsView);
        produitsView.setPrefSize(800, 600);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void handleCommandesButtonClick() {
        commandeController.afficherCommandes(tableView);
        Scene scene = new Scene(tableView);
        tableView.setPrefSize(800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
