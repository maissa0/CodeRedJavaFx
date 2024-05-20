package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Commande;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.RecetteService;
import edu.CodeRed.services.ServiceObjectif;
import edu.CodeRed.services.userservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DashboardController implements Initializable {

    @FXML
    private PieChart CategoriePieChart;

    @FXML
    private PieChart genderPieChart1;
    private RecetteService rs = new RecetteService();
    private userservice us = new userservice();

    private ServiceObjectif os =new ServiceObjectif();

    @FXML
    private PieChart pieChart;
    private ProduitController produitController;
    private CommandeController commandeController;
    TableView<Commande> tableView = new TableView<>();
    @FXML
    private VBox produitsView;
    @FXML
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatePieChart();
        updatePieChartgender();
        updatePieChartchoix();
        produitsView = new VBox();
        try {
            produitController = new ProduitController();
            commandeController = new CommandeController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        stage =new Stage();

    }

    private void updatePieChart() {
        if (CategoriePieChart != null) {
            CategoriePieChart.getData().clear();
            Map<String, Integer> categorieCounts = rs.getingredientCounts();
            for (String categorie : categorieCounts.keySet()) {
                PieChart.Data slice = new PieChart.Data(categorie, categorieCounts.get(categorie));
                CategoriePieChart.getData().add(slice);
            }
        }
    }


    private void updatePieChartgender() {
        if (genderPieChart1 != null) {
            genderPieChart1.getData().clear();
            Map<String, Integer> genderCounts = us.getuserCounts();
            for (String genre : genderCounts.keySet()) {
                PieChart.Data slice = new PieChart.Data(genre, genderCounts.get(genre));
                genderPieChart1.getData().add(slice);
            }
        }
    }private void updatePieChartchoix() {
        if (pieChart != null) {
            pieChart.getData().clear();
            Map<String, Integer> genderCounts = os.getobjCounts();
            for (String choix : genderCounts.keySet()) {
                PieChart.Data slice = new PieChart.Data(choix, genderCounts.get(choix));
                pieChart.getData().add(slice);
            }
        }
    }


    Preferences p = Preferences.userNodeForPackage(getClass());
    edu.CodeRed.entities.user user= new user();
    @FXML
    void back_to_login(ActionEvent event) throws IOException {

        p.getInt("userId", 0);
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/login.fxml"));
        //searchbar.getChildren().setAll(pane);
        System.out.println(p.getInt("userId hedhi logout", user.getId()));

    }



    @FXML
    void openViewActitvite(ActionEvent event) throws IOException {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterActivite.fxml"));
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
    void openViewCommande(ActionEvent event) {



        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        commandeController.afficherCommandes(tableView);
        Scene scene = new Scene(tableView);
        tableView.setPrefSize(1023, 612);
        stage.setScene(scene);

        // Close the current stage

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewIngredient(ActionEvent event) throws IOException {

        // Get the MenuItem that was clicked
        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ingredient.fxml"));
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
    void openViewObjectif(ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CRUD.fxml"));
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
    void openViewProduit(ActionEvent event) {



        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        produitController.afficherProduits(produitsView);
        Scene scene = new Scene(produitsView);
        produitsView.setPrefSize(1023, 612);
        stage.setScene(scene);


        // Close the current stage


        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewRecette(ActionEvent event) throws IOException {

        // Get the MenuItem that was clicked
        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewRecette.fxml"));
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
    void openViewSuiviObj(ActionEvent event) throws IOException {

        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CRUDSUIVI.fxml"));
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
    void openViewUser(ActionEvent event) throws IOException {


        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/listUser.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        // Close the current stage
        //currentStage.close();

        // Show the new stage
        stage.show();
    }

}
