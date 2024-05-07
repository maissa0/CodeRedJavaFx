package edu.CodeRed.controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.CodeRed.entities.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import edu.CodeRed.services.userservice;

public class registerUser {
    @FXML
    private TextField adresse_input;

    @FXML
    private DatePicker birthday_input;

    @FXML
    private TextField email_input;

    @FXML
    private Text error;
    @FXML
    private ComboBox<String> role_combobox;

    @FXML
    private ComboBox<String> gender_combobox;

    @FXML
    private TextField mdp_input;
    @FXML
    private TextField id_input;
    @FXML
    private TextField nom_input;

    @FXML
    private TextField numtel_input;

    @FXML
    private TextField prenom_input;

    @FXML
    private PasswordField passwordconfirmation_input;


    @FXML
    private TextField phonenumber_input;


    @FXML
    void back_to_login(ActionEvent event) throws IOException {
        Home.loadFXML("/login.fxml");
    }
    @FXML
    void initialize() {
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Female", "Male");
        gender_combobox.setItems(genderOptions);
        ObservableList<String> roleOptions = FXCollections.observableArrayList("user", "admin");
        role_combobox.setItems(roleOptions);

    }
    @FXML
    void reset_inputs(ActionEvent event) {
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
    void submit_user(ActionEvent event) throws IOException, SQLException {
        if(validateForm()) {
            LocalDate selectedDate = birthday_input.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedBirthday = selectedDate.format(formatter);
            String selectedRole = role_combobox.getValue();
            String selectedGender = gender_combobox.getValue();
            user user = new user(email_input.getText(),nom_input.getText(), prenom_input.getText(),formattedBirthday, selectedRole, selectedGender,numtel_input.getText(),  adresse_input.getText(), mdp_input.getText()
            );
            userservice userservice = new userservice(); // Crée une nouvelle instance de la classe UserService
            userservice.addUser(user);
        }
    }
    private boolean validateForm() {
        if (nom_input.getText().isEmpty() || email_input.getText().isEmpty() || mdp_input.getText().isEmpty() ||
                prenom_input.getText().isEmpty() || adresse_input.getText().isEmpty() ||
                birthday_input.getValue() == null || role_combobox.getValue() == null ||gender_combobox.getValue() == null ) {
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

        if (!mdp_input.getText().equals(passwordconfirmation_input.getText())) {
            error.setText("Your password is not the same as the confirmation");
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

    //Methode to generate user password
    //WIP
    public static String generatePassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialCharacters = "@$!%*?&";
        String allCharacters = uppercaseLetters + digits + specialCharacters;
        int passwordLength = 12;
        StringBuilder password = new StringBuilder(passwordLength);
        SecureRandom random = new SecureRandom();
        password.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
        for (int i = 3; i < passwordLength; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
        return password.toString();
    }
   }


