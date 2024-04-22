package edu.CodeRed.Controllers;
import edu.CodeRed.services.userservice; // Assurez-vous que le chemin est correct

import java.io.IOException;
import edu.CodeRed.entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
    public class AddUser {

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
        void back_to_list(ActionEvent event) {

        }





        @FXML
        void reset_input(ActionEvent event) {
            nom_input.clear();
            birthday_input.setValue(null);
            email_input.clear();
            prenom_input.clear();
            gender_combobox.setValue(null);
            mdp_input.clear();
            adresse_input.clear();
            numtel_input.clear();
        }
        @FXML
        void submit_user(ActionEvent event) throws IOException, SQLException {
            if(validateForm()) {
                LocalDate selectedDate = birthday_input.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedBirthday = selectedDate.format(formatter);
                String selectedGender = gender_combobox.getValue();
                user user = new user(nom_input.getText(), email_input.getText(), mdp_input.getText(), prenom_input.getText(), adresse_input.getText(),
                        formattedBirthday, selectedGender, numtel_input.getText());
                userservice userService = new userservice(); // Crée une nouvelle instance de la classe UserService
                userService.addUser(user);
            }
        }
        private boolean validateForm() {
            if (nom_input.getText().isEmpty() || email_input.getText().isEmpty() || mdp_input.getText().isEmpty() ||
                    prenom_input.getText().isEmpty() || adresse_input.getText().isEmpty() ||
                    birthday_input.getValue() == null || gender_combobox.getValue() == null ) {
                error.setText("All fields must be filled");
                error.setVisible(true);
                return false;
            }

            String email = email_input.getText();
            if (!isEmailValid(email)) {
                error.setText("Invalid email address");
                error.setVisible(true);
                return false;
            }

            if (!isValidPassword(mdp_input.getText())) {
                error.setText("Password must contain at least one uppercase letter, one number, and one special character");
                error.setVisible(true);
                return false;
            }
            return true;
        }
        //Password validation methode
        private boolean isValidPassword(String password) {
            String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!/])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
        //Email validation methode
        private boolean isEmailValid(String email) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        @FXML
        void initialize() {
            ObservableList<String> genderOptions = FXCollections.observableArrayList("Femme", "Homme");
            gender_combobox.setItems(genderOptions);

        }
    }
