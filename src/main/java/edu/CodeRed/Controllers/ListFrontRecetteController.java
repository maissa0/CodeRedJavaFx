package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import edu.CodeRed.entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;



public class ListFrontRecetteController implements Initializable {

    @FXML
    private ScrollPane Rec_ScrollPane;

    @FXML
    private GridPane Recette_gridPane;

    @FXML
    private GridPane Recette_gridPaneSuggest;

    @FXML
    private Pagination pag;

    @FXML
    private ImageView picture;
    @FXML
    private Pagination pagSug;

    RecetteService rs = new RecetteService();

    JournalService js = new JournalService();
    List<Recette> recetteList = rs.getAllDataRecette();

    private static int idRecette;

    public int getIdRecette(){
        return this.idRecette;
    }

    public void setIdRecette(int id){
        this.idRecette=id;
        System.out.println("Set Recipe ID: " + idRecette);
    }

    Preferences p = Preferences.userNodeForPackage(getClass());
    user user= new user();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Received Recipe ID: " + idRecette);
        try {
            // Get the top 5 most used recettes
            List<Recette> top5Recettes = js.getTop5MostUsedRecettes(p.getInt("userId", user.getId()));

            // Add the top 5 most used recettes to the Recette_gridPaneSuggest
            int totalPagessug = (int) Math.ceil(top5Recettes.size() / 6.0);

            // Set the total number of pages
            pagSug.setPageCount(totalPagessug);

            // Set the page factory to dynamically load cards for each page
            pagSug.setPageFactory(pageIndex -> {
                // Create a new GridPane to hold the cards for the current page
                GridPane pageGridPane = new GridPane();
                pageGridPane.setHgap(10); // Horizontal gap between cards
                pageGridPane.setVgap(10); // Vertical gap between cards
                pageGridPane.setPadding(new Insets(10)); // Padding for the entire page

                // Calculate the starting index and ending index of recipes for the current page
                int startIndex = pageIndex * 6;
                int endIndex = Math.min(startIndex + 6, top5Recettes.size());

                // Loop through the recipes for the current page
                for (int i = startIndex; i < endIndex; i++) {
                    // Load the FXML file for the recipe card
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CardDesignRecetteFront.fxml"));
                    AnchorPane anchorPane;
                    try {
                        anchorPane = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Get the controller of the loaded FXML
                    CardDesignRecetteFrontController itemController = fxmlLoader.getController();

                    // Set the recipe data for the current card
                    itemController.setRecetteDataF(top5Recettes.get(i));

                    // Set the recipe ID in the controller
                    itemController.setIdRecette(top5Recettes.get(i).getId());

                    // Calculate the row and column indices for the current card in the GridPane
                    int row = (i - startIndex) / 3; // Three cards per row
                    int col = (i - startIndex) % 3;

                    // Add the card to the GridPane at the calculated row and column indices
                    pageGridPane.add(anchorPane, col, row);
                }

                // Return the GridPane containing the cards for the current page
                return pageGridPane;
            });

            // Calculate the number of pages needed
            int totalPages = (int) Math.ceil(recetteList.size() / 6.0);

            // Set the total number of pages
            pag.setPageCount(totalPages);

            // Set the page factory to dynamically load cards for each page
            pag.setPageFactory(pageIndex -> {
                // Create a new GridPane to hold the cards for the current page
                GridPane pageGridPane = new GridPane();
                pageGridPane.setHgap(10); // Horizontal gap between cards
                pageGridPane.setVgap(10); // Vertical gap between cards
                pageGridPane.setPadding(new Insets(10)); // Padding for the entire page

                // Calculate the starting index and ending index of recipes for the current page
                int startIndex = pageIndex * 6;
                int endIndex = Math.min(startIndex + 6, recetteList.size());

                // Loop through the recipes for the current page
                for (int i = startIndex; i < endIndex; i++) {
                    // Load the FXML file for the recipe card
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CardDesignRecetteFront.fxml"));
                    AnchorPane anchorPane;
                    try {
                        anchorPane = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Get the controller of the loaded FXML
                    CardDesignRecetteFrontController itemController = fxmlLoader.getController();

                    // Set the recipe data for the current card
                    itemController.setRecetteDataF(recetteList.get(i));

                    // Set the recipe ID in the controller
                    itemController.setIdRecette(recetteList.get(i).getId());

                    // Calculate the row and column indices for the current card in the GridPane
                    int row = (i - startIndex) / 3; // Three cards per row
                    int col = (i - startIndex) % 3;

                    // Add the card to the GridPane at the calculated row and column indices
                    pageGridPane.add(anchorPane, col, row);
                }

                // Return the GridPane containing the cards for the current page
                return pageGridPane;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ///////////////////////////////////////eYAA

        produitsViewFront=new VBox();
        try {
            produitController = new ProduitController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stage=new Stage();
        panierView = new VBox();
        try {
            panierController=new PanierController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        panierScene = new Scene(panierView);
    }

        ///////////////////////////////////sidebar

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


