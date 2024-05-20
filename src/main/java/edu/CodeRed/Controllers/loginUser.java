package edu.CodeRed.Controllers;
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

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.prefs.Preferences;

public class loginUser {




    @FXML
    private TextField password_input;
    @FXML
    private TextField email_input;
    @FXML
    private Text error;
    private userservice userservice = new userservice();

    Preferences p = Preferences.userNodeForPackage(getClass());
    user u =new user();


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
        p.putInt("userId", user.getId());
        p.put("daten", user.getDate_de_naissance());
        p.put("sexe ", user.getDate_de_naissance());



        System.out.println(p.get("daten", user.getDate_de_naissance()));


        if (user != null) {


            UserSession.getInstance(user.getId(), user.getEmail(), user.getPassword(), user.getNom(), user.getPrenom(),
                    user.getDate_de_naissance(), user.getRole(), user.getGenre(), user.getAdresse(), user.getNum_de_telephone());

            System.out.println(p.getInt("userId taa login", user.getId()));

            if (Objects.equals(user.getRole(), "admin" )||Objects.equals(user.getRole(), "[\"ROLE_ADMIN\"]" )) {
                try {

                     Parent root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (Objects.equals(user.getRole(), "user")|| Objects.equals(user.getRole(), "[\"ROLE_USER\"]" )) {
                try {

                     Parent root = FXMLLoader.load(getClass().getResource("/front.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/registerUser.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
     }

    @FXML
    void redirect_passwordpage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forgotPassword.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
     }
}

