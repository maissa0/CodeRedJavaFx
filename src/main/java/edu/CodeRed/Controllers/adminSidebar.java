package edu.CodeRed.controllers;

import io.jsonwebtoken.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class adminSidebar {

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
    void openViewIngredient(ActionEvent event) {

    }

    @FXML
    void openViewObjectif(ActionEvent event) {

    }

    @FXML
    void openViewProduit(ActionEvent event) {

    }

    @FXML
    void openViewRecette(ActionEvent event) {

    }

    @FXML
    void openViewSuiviObj(ActionEvent event) {

    }

    @FXML
    void openViewUser(ActionEvent event) {

    }
    @FXML
    void back_to_login(ActionEvent event) throws IOException, java.io.IOException {
        Home.loadFXML("/login.fxml");
    }}

