package edu.CodeRed.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class adminSidebar {
    @FXML
    public Label welcome;

    @FXML
    private Label role;

    @FXML
    private Label username;

    UserSession userSession;

    public adminSidebar(int id, String email, String password, String nom, String prenom, String date_de_naissance, String genre, String num_de_telephone, String role) {
        userSession = UserSession.getInstance(id, email, password, nom, prenom, date_de_naissance, genre, num_de_telephone, role);
    }

    @FXML
    void disconnect(ActionEvent event) throws IOException {
        userSession.cleanUserSession();
        Home.loadFXML("/login.fxml");
    }

    @FXML
    void goto_blog(ActionEvent event) throws IOException {
        Home.loadFXML("/login.fxml");
    }

    @FXML
    void goto_dashboard(ActionEvent event) throws IOException {
        Home.loadFXML("/adminDashboard.fxml");
    }

    @FXML
    void goto_event(ActionEvent event) throws IOException {
        Home.loadFXML("/listUser.fxml");
    }

    @FXML
    void goto_forum(ActionEvent event) throws IOException {
        Home.loadFXML("/login.fxml");
    }

    @FXML
    void goto_shop(ActionEvent event) throws IOException {
        Home.loadFXML("/login.fxml");
    }

    @FXML
    void goto_user(ActionEvent event) throws IOException {
        Home.loadFXML("/listUser.fxml");
    }
}
