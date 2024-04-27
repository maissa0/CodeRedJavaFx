package edu.CodeRed.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminSideBarController {

    @FXML
    private Label level;

    @FXML
    private ImageView picture;

    @FXML
    private Label role;


    @FXML
    private Label welcome;

    @FXML
    void GoToIngredients(ActionEvent event) throws IOException {
        Home.loadFXML((Stage) picture.getScene().getWindow(), "/ingredient.fxml");
        //Home.loadFXML("/ingredient.fxml");
    }
    @FXML
    void GoToRecettes(ActionEvent event) throws IOException {
        Home.loadFXML((Stage) picture.getScene().getWindow(), "/viewRecette.fxml");
    }

}