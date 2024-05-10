package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class showDetailsJournalCalController implements Initializable {

    @FXML
    private Label labelCaloriesJournal;

    @FXML
    private Label labelDateJournal;

    @FXML
    private VBox vBoxRecettes;

    private int selectedJournalId;

    public int getSelectedJournalId() {
        return this.selectedJournalId;
    }

    Journal j;

    public void setIdJournal(int selectedJournalId) {
        this.selectedJournalId = selectedJournalId;
        System.out.println("Set Rijiji ID: " + selectedJournalId);

        // Call the method to initialize recipe details
        initializeJourDetails();
    }

    private void initializeJourDetails() {
        JournalService rs=new JournalService();
        try {
            j = rs.findById(selectedJournalId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        labelDateJournal.setText((String.valueOf(j.getDate())));
        labelCaloriesJournal.setText(String.valueOf(j.getCaloriesJournal()));

        System.out.println(j.getId());

        List<Recette> recList = new ArrayList<>();
        recList=rs.getRecettesForJournal(j.getId());
        for(int i=0;i<recList.size();i++){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/itemRecette.fxml"));
                AnchorPane pane = load.load();
                itemRecetteController item = load.getController();
                item.setData(recList.get(i));
                vBoxRecettes.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setIdJournal(selectedJournalId);

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
}
