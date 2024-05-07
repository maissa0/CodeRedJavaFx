package edu.CodeRed.controllers;
import edu.CodeRed.entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import edu.CodeRed.services.userservice;
import javafx.stage.Stage;

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
        // Check for null inputs
        if (email_input.getText() == null || password_input.getText() == null) {
            // Handle null input (e.g., display error message)
            return;
        }

        String userName = email_input.getText();
        String password = password_input.getText();

        user user = userservice.loginUser(userName, password);
        if (user != null) {
            UserSession.getInstance(user.getId(), user.getEmail(), user.getPassword(), user.getNom(), user.getPrenom(),
                    user.getDate_de_naissance(), user.getRole(), user.getGenre(), user.getAdresse(), user.getNum_de_telephone());

            if (Objects.equals(user.getRole(), "admin")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/listUser.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(user.getRole(), "user")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            error.setText("Your email or password are incorrect");
            error.setVisible(true);
        }
    }

    /*@FXML
    void login(ActionEvent event) throws SQLException {
        try {
            user user;
            if ((user = userservice.loginUser(email_input.getText(), password_input.getText()))!=null) {
                UserSession.getInstance(user.getId(),user.getEmail(), user.getPassword(),user.getNom(),user.getPrenom(),
                        user.getDate_de_naissance(),user.getRole(),user.getGenre(),user.getAdresse(),user.getNum_de_telephone());

                if(Objects.equals(user.getRole(), "admin")){
                    Home.loadFXML("/listUser.fxml");
                }

            } else {
                error.setText("Your email or password are incorrect");
                error.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error.setVisible(true);
        }
    }

*/

    @FXML
    void register_page(ActionEvent event) throws IOException {
        Home.loadFXML("/registerUser.fxml");
    }

    @FXML
    void redirect_passwordpage(ActionEvent event) throws IOException {
        Home.loadFXML("/forgotPassword.fxml");
    }
}
