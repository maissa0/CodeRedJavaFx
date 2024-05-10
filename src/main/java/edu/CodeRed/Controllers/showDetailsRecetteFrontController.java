package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class showDetailsRecetteFrontController implements Initializable {

    @FXML
    private Label Nom;

    @FXML
    private MenuItem gesIng;

    @FXML
    private MenuItem gesIng1;

    @FXML
    private MenuItem gesIng11;

    @FXML
    private MenuItem getRec;

    @FXML
    private MenuItem getRec1;

    @FXML
    private MenuItem getRec11;

    @FXML
    private Label labelCalorieRecette;

    @FXML
    private Label labelDescriptionRecette;

    @FXML
    private ImageView labelImgRecette;

    @FXML
    private Label labelNomRecette;

    @FXML
    private ImageView picture;

    @FXML
    private ProgressBar progbardets;

    @FXML
    private Label role;

    @FXML
    private VBox vBoxIng;




    Recette r;
    private int idRecette; // Variable to hold the idRecette

    // Getter and setter for idRecette
    public int getIdRecette() {
        return idRecette;
    }

    public void setIdRecette(int idRecette) {
        this.idRecette = idRecette;
        System.out.println("Set Recipe ID: " + idRecette);

        // Call the method to initialize recipe details
        initializeRecipeDetails();
    }

    // Method to initialize recipe details
    private void initializeRecipeDetails() {
        RecetteService rs=new RecetteService();
        try {
            r = rs.findById(idRecette);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //labelDescriptionRecette.setWrapText(true);
        labelNomRecette.setText(r.getNom());
       // labelCategRecette.setText(r.getCategorie());
        labelImgRecette.setImage(new Image("file:C:/tools/optihealth/src/main/java/edu/CodeRed/uploads/" + r.getImage()));
        labelDescriptionRecette.setText(r.getDescription());
        labelCalorieRecette.setText(String.valueOf(r.getCalorieRecette()));

        if ("Facile".equals(r.getCategorie())) {
            progbardets.setProgress(0.3);

            // Apply custom style to change the color to green
            progbardets.setStyle("-fx-accent: green;");

        }
        else if ("Moyenne".equals(r.getCategorie())){

            progbardets.setProgress(0.5);

            // Apply custom style to change the color to green
            progbardets.setStyle("-fx-accent: orange;");


        }
        else{
            progbardets.setProgress(1);

            // Apply custom style to change the color to green
            progbardets.setStyle("-fx-accent: red;");

        }


        System.out.println(r.getId());



        List<Ingredient> ingList = new ArrayList<>();
        ingList=rs.getIngredientsForRecette(r.getId());
        for(int i=0;i<ingList.size();i++){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/itemIngredient.fxml"));
                AnchorPane pane = load.load();
                itemIngredientController item = load.getController();
                item.setData(ingList.get(i));
                vBoxIng.getChildren().add(pane);
                vBoxIng.setSpacing(10);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        currentStage.close();

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


    private VBox produitsViewFront;

    private Stage stage;
    private ProduitController produitController;
    @FXML
    private VBox panierView;
    private PanierController panierController;
    private Scene panierScene; // On garde une référence à la scène du panier
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
        produitsViewFront.setPrefSize(800, 600);
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Nothing needs to be done here since idRecette is set externally

        produitsViewFront=new VBox();
        try {
            produitController = new ProduitController();
            panierController=new PanierController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stage=new Stage();
        panierView = new VBox();

        panierScene = new Scene(panierView);
    }
}




