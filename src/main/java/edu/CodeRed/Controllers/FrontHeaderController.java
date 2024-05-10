package edu.CodeRed.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class FrontHeaderController {

    @FXML
    private AnchorPane recpane;

    @FXML
    void objectiffront(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/SUIVIOBJ.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewCommand(ActionEvent event) {

    }

    @FXML
    void openViewJournal(ActionEvent event)  throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/viewJournal.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewPanier(ActionEvent event) {
        panierController.afficherProduitsDuPanier();
        Scene scene = new Scene(panierView);

        panierView.setPrefSize(800, 600);

        stage.setScene(panierScene);

        stage.show();
    }

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
    void openViewProduct(ActionEvent event) {

        produitController.afficherProduitsFront(produitsViewFront);
        Scene scene = new Scene(produitsViewFront);
        produitsViewFront.setPrefSize(800, 600);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void openViewRecettes(ActionEvent event)  throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void openViewSuivAct(ActionEvent event)throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/AjouterSuivi.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewSuivObj(ActionEvent event)  throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/ADDCRUD.fxml"));
        recpane.getChildren().setAll(pane);
    }
}
