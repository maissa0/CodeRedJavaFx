package edu.CodeRed.Controllers;

import edu.CodeRed.entities.user;
import edu.CodeRed.services.userservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class UpdateUser {

    @FXML
    private TextField adresse_input;

    @FXML
    private DatePicker birthday_input;

    @FXML
    private TextField email_input;

    @FXML
    private Text error;

    @FXML
    private ComboBox<String> gender_combobox;

    @FXML
    private TextField mdp_input;

    @FXML
    private TextField nom_input;

    @FXML
    private TextField numtel_input;

    @FXML
    private TextField prenom_input;

    @FXML
    private ComboBox<String> role_combobox;

    private user User; // Declare user variable
    private ListUser listUserController; // Declare listUserController variable
    private userservice userService = new userservice();

    @FXML
    public void setUser(user User) {
        this.User = User;

        // Initialize the fields with the values of the user
        adresse_input.setText(User.getAdresse());
        email_input.setText(User.getEmail());
        numtel_input.setText(User.getNum_de_telephone());
        prenom_input.setText(User.getPrenom());
        nom_input.setText(User.getNom());
        role_combobox.setValue(User.getRole());
        mdp_input.setText(User.getPassword());
        gender_combobox.setValue(User.getGenre());

        // Parse the string representation of the date into a LocalDate object
        if (User.getDate_de_naissance() != null && !User.getDate_de_naissance().isEmpty()) {
          //  LocalDate dateNaissance = LocalDate.parse(User.getDate_de_naissance());
            // Set the parsed LocalDate to the birthday_input DatePicker
          //  birthday_input.setValue(dateNaissance);
        }
    }

    @FXML
    void back_to_list(ActionEvent event) throws IOException {
     }

    @FXML
    void reset_input(ActionEvent event) {
        nom_input.clear();
        birthday_input.setValue(null);
        email_input.clear();
        prenom_input.clear();
        role_combobox.setValue(null);
        gender_combobox.setValue(null);
        mdp_input.clear();
        adresse_input.clear();
        numtel_input.clear();
    }

    @FXML
    void modify_user(ActionEvent event) throws IOException {
        if (User == null) {
            // If the user object is null, show an error message
            showErrorAlert("User not set", "User object is null.");
            return;
        }

        // Retrieve values from input fields
        String adresse = adresse_input.getText();
        LocalDate birthday = birthday_input.getValue();
        String email = email_input.getText();
        String genre = gender_combobox.getValue();
        String nom = nom_input.getText();
        String prenom = prenom_input.getText();
        String numtel = numtel_input.getText();
        String role = role_combobox.getValue();

        // Update the User object with the new values
        User.setAdresse(adresse);
        // Ensure birthday is not null before converting to string
        if (birthday != null) {
            User.setDate_de_naissance(birthday.toString());
        }
        User.setEmail(email);
        User.setGenre(genre);
        User.setNom(nom);
        User.setPrenom(prenom);
        User.setNum_de_telephone(numtel);
        User.setRole(role);

        // Perform actions to update user data in the database or send it to a server
        // Example: You can call a service method to update the user data
        if (userService != null) {
            // Call the updateUser method on the userService instance
            userService.UpdatUser(User);

            // Display a message to indicate that the user details have been modified
            showAlert(Alert.AlertType.INFORMATION, "User Modification", null, "User details have been modified successfully.");

            // After modifying the user details, navigate back to the list view
            if (listUserController != null) {
                listUserController.refreshList();
            }
        } else {
            // If userService is not available, display an error message
            showErrorAlert("Error", "Failed to update user details. User service is not available.");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/listUser.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void setListUserController(ListUser listUserController) {
        this.listUserController = listUserController;
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, null, content);
    }
}
