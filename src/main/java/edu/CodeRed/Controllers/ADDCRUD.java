package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Objectif;
import edu.CodeRed.services.ServiceObjectif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import edu.CodeRed.entities.user;


public class ADDCRUD {
    @FXML
    private ComboBox<String> tActivity_level;
    @FXML
    private ComboBox<String> tObjectif;

    @FXML
    private ComboBox<String> tSexe;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button produitsButton;
    @FXML
    private Button panierButton;
    @FXML
    private VBox produitsView;
    private VBox produitsViewFront;

    private Stage stage;
    private ProduitController produitController;
    @FXML
    private VBox panierView;
    private PanierController panierController;
    private Scene panierScene;


    @FXML
    private TextField tAge;

    @FXML
    private TextField tCalorie;

    @FXML
    private TextField tHeight;

    @FXML
    private TextField tID;

    @FXML
    private Button calculateBtn;
    @FXML
    private Button calculateIMCBtn;
    Preferences p = Preferences.userNodeForPackage(getClass());
    user user= new user();



    @FXML
    private TextField tWeight;

    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);






    ServiceObjectif os = new ServiceObjectif();

    public void initialize() throws SQLException {
        // Initialisation des ComboBox
        String[] items = { "Homme", "Femme"};
        tSexe.getItems().addAll(items);
        String[] items2 = { "Sédentaire ", "Léger ","Modéré ","Actif ","Très_actif"};
        tActivity_level.getItems().addAll(items2);
        String[] items3 = { "perdre_de_poids", "gagne_de_poids"};
        tObjectif.getItems().addAll(items3);

        // Validation pour les champs age, weight, height, et calorie
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            Pattern validEditingState = Pattern.compile("-?\\d*");

            String newText = change.getControlNewText();
            if (validEditingState.matcher(newText).matches()) {
                return change;
            } else {
                return null;
            }
        };

        tAge.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        tCalorie.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        tHeight.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        tWeight.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        calculateBtn.setOnAction(this::calculateCalories);
        calculateIMCBtn.setOnAction(this::calculateIMC);


        produitsViewFront=new VBox();
        produitController = new ProduitController();
        stage=new Stage();
        panierView = new VBox();
        panierController=new PanierController();
        panierScene = new Scene(panierView);
    }
    @FXML
    void AddObjectif(ActionEvent event) {


        if (tAge.getText().isEmpty() || tWeight.getText().isEmpty() || tHeight.getText().isEmpty() || tCalorie.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tu ne peux pas faire ça. Assure-toi de remplir tous les champs.");
            alert.showAndWait();
            return;
        }


            Objectif o =new Objectif(
                    p.getInt("userId", user.getId()),
                    tSexe.getValue(),
                    Integer.parseInt(tAge.getText()),
                    Integer.parseInt(tWeight.getText()),
                    Integer.parseInt(tHeight.getText()),
                    tActivity_level.getValue(),
                    tObjectif.getValue(),
                    Integer.parseInt(tCalorie.getText()));
            try {
                os.add(o);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("User insérée avec succés!");
                alert.show();

                initialize();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText(e.getMessage());
                alert.show();
                throw new RuntimeException(e);
            }
        }

    @FXML
    void calculateCalories(ActionEvent event) {
        if (tAge.getText().isEmpty() || tWeight.getText().isEmpty() || tHeight.getText().isEmpty() || tActivity_level.getValue() == null || tSexe.getValue() == null || tObjectif.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Calcul des calories en fonction des données
        int age = Integer.parseInt(tAge.getText());
        int weight = Integer.parseInt(tWeight.getText());
        int height = Integer.parseInt(tHeight.getText());
        String activityLevel = tActivity_level.getValue();
        String sex = tSexe.getValue();
        String objective = tObjectif.getValue();

        double bmr;
        if (sex.equals("Homme")) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        double calories;
        switch (activityLevel) {
            case "Sédentaire":
                calories = bmr * 1.2;
                break;
            case "Léger":
                calories = bmr * 1.375;
                break;
            case "Modéré":
                calories = bmr * 1.55;
                break;
            case "Actif":
                calories = bmr * 1.725;
                break;
            case "Très_actif":
                calories = bmr * 1.9;
                break;
            default:
                calories = 0;
                break;
        }

        if (objective.equals("perdre_de_poids")) {
            calories -= 500; // Déficit calorique de 500 calories par jour pour perdre du poids
        } else if (objective.equals("gagne_de_poids")) {
            calories += 500; // Excès calorique de 500 calories par jour pour gagner du poids
        }

        tCalorie.setText(String.valueOf((int) calories));

        // Calcul des calories pour maintenir le poids actuel
        double maintainCalories = bmr * 1.2; // Calcul pour une activité sédentaire
        tCalorie.setText(String.valueOf((int) maintainCalories));
    }
    @FXML
    void calculateIMC(ActionEvent event) {
        if (tWeight.getText().isEmpty() || tHeight.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir les champs de poids et de taille.");
            alert.showAndWait();
            return;
        }

        double weight = Double.parseDouble(tWeight.getText());
        double height = Double.parseDouble(tHeight.getText()) / 100; // Convertir la taille en mètres

        double imc = weight / (height * height);

        String message;
        if (imc < 18.5) {
            message = "Votre IMC est " + String.format("%.2f", imc) + ", vous êtes en état de maigreur.";
        } else if (imc < 25) {
            message = "Votre IMC est " + String.format("%.2f", imc) + ", vous avez une corpulence normale.";
        } else if (imc < 30) {
            message = "Votre IMC est " + String.format("%.2f", imc) + ", vous êtes en surpoids.";
        } else {
            message = "Votre IMC est " + String.format("%.2f", imc) + ", vous êtes en état d'obésité.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Indice de Masse Corporelle (IMC)");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void suiviobjfront(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SUIVIOBJ.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ouverture de la fenêtre");
            alert.setContentText("Une erreur s'est produite lors de l'ouverture de la fenêtre. Veuillez réessayer.");
            alert.showAndWait();
        }
    }


    @FXML
    void objectiffront(ActionEvent event) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ADDCRUD.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewCommand(ActionEvent event) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        //currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewJournal(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewJournal.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }


    // On garde une référence à la scène du panier
    @FXML
    void openViewPanier(ActionEvent event) throws IOException {


        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        panierController.afficherProduitsDuPanier();
        Scene scene = new Scene(panierView);

        panierView.setPrefSize(800, 600);

        stage.setScene(panierScene);



        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewProduct(ActionEvent event) throws IOException {


        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        produitController.afficherProduitsFront(produitsViewFront);
        Scene scene = new Scene(produitsViewFront);
        produitsViewFront.setPrefSize(1095, 487);
        stage.setScene(scene);


        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewRecettes(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewSuivAct(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSuivi.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewSuivObj(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SUIVIOBJ.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

}
