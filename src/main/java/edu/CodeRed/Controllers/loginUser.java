package edu.CodeRed.controllers;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.userservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class loginUser {

    @FXML
    private TextField password_input;
    @FXML
    private TextField email_input;
    @FXML
    private Text error;
    private userservice userservice = new userservice();

    @FXML
    void login(ActionEvent event) throws SQLException {
        try {
            user user;
            if ((user = userservice.loginUser(email_input.getText(), password_input.getText()))!=null) {
                UserSession.getInstance(user.getId(),user.getEmail(), user.getPassword(),user.getNom(),user.getPrenom(),user.getDate_de_naissance(),user.getGenre(),user.getNum_de_telephone(),user.getrole());
                if(Objects.equals(user.getrole(), "admin"))
                    Home.loadFXML("/adminDashboard.fxml");
            } else {
                error.setText("Your Email or password are incorrect");
                error.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error.setVisible(true);
        }
    }
    @FXML
    void register_page(ActionEvent event) throws IOException {
        Home.loadFXML("/registerUser.fxml");
    }
    @FXML
    void initialize() {
        error.setVisible(false);
    }
    @FXML
    void redirect_passwordpage(ActionEvent event) throws IOException {
        Home.loadFXML("/forgotPassword.fxml");
    }
}