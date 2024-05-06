package edu.CodeRed.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminSideBarController {

    @FXML
    private Label Nom;

    @FXML
    private MenuItem gesIng;

    @FXML
    private MenuItem gesIng1;

    @FXML
    private MenuItem gesIng11;

    @FXML
    private MenuItem getRec;

    @FXML
    private MenuItem getRec1;

    @FXML
    private MenuItem getRec11;

    @FXML
    private ImageView picture;

    @FXML
    private Label role;

    @FXML
    void openViewActitvite(ActionEvent event) {

    }

    @FXML
    void openViewCommande(ActionEvent event) {

    }

    @FXML
    void openViewIngredient(ActionEvent event) throws IOException {

        Home.loadFXML((Stage) picture.getScene().getWindow(), "/ingredient.fxml");
    }

    @FXML
    void openViewObjectif(ActionEvent event) {

    }

    @FXML
    void openViewProduit(ActionEvent event) {

    }

    @FXML
    void openViewRecette(ActionEvent event) throws IOException {

        Home.loadFXML((Stage) picture.getScene().getWindow(), "/viewRecette.fxml");
    }

    @FXML
    void openViewSuiviObj(ActionEvent event) {

    }

    @FXML
    void openViewUser(ActionEvent event) {

    }

}
