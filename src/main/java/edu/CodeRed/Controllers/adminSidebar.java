package edu.CodeRed.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class adminSidebar {

    @FXML
    private Label level;

    @FXML
    private ImageView picture;

    @FXML
    private Label role;

    @FXML
    private Label username;

    @FXML
    private Label welcome;

    @FXML
    void go_toRecetteAdd(ActionEvent event) throws IOException {
        edu.CodeRed.controllers.Home.loadFXML("/AddRecette.fxml");

    }

}