package edu.CodeRed.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class front {
    @FXML
    private Button produitsButton;
    @FXML
    private Button panierButton;
    @FXML
    private VBox produitsView;
    private VBox produitsViewFront;

    private Stage stage;
    private ProduitController produitController;
    @FXML
    private VBox panierView;
    private PanierController panierController;
    private Scene panierScene; // On garde une référence à la scène du panier

    public void initialize() throws SQLException {

        produitsViewFront=new VBox();
        produitController = new ProduitController();
        stage=new Stage();
        panierView = new VBox();
        panierController=new PanierController();
        panierScene = new Scene(panierView);

    }

    @FXML
    private void handleProduitsButtonClick() {
        produitController.afficherProduitsFront(produitsViewFront);
        Scene scene = new Scene(produitsViewFront);
        produitsViewFront.setPrefSize(800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handlePanierButtonClick() {
        panierController.afficherProduitsDuPanier();
        Scene scene = new Scene(panierView);

        panierView.setPrefSize(800, 600);

        stage.setScene(panierScene);

        stage.show();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    void objectiffront(ActionEvent event) {

    }

    @FXML
    void openViewCommand(ActionEvent event) {

    }

    @FXML
    void openViewJournal(ActionEvent event) {

    }

    @FXML
    void openViewPanier(ActionEvent event) {

    }

    @FXML
    void openViewProduct(ActionEvent event) {

    }

    @FXML
    void openViewRecettes(ActionEvent event) {

    }

    @FXML
    void openViewSuivAct(ActionEvent event) {

    }

    @FXML
    void openViewSuivObj(ActionEvent event) {

    }

}

