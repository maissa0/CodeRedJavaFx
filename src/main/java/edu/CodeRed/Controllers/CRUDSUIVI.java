package edu.CodeRed.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import edu.CodeRed.entities.Commande;
import edu.CodeRed.entities.SuivieObjectif;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.ServiceObjectif;
import edu.CodeRed.services.ServiceSuiviObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class CRUDSUIVI {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<SuivieObjectif, Integer> Cancien;

    @FXML
    private TableColumn<SuivieObjectif, Date> Cdate;

    @FXML
    private TableColumn<SuivieObjectif, Float> Cnouveau;

    @FXML
    private TableColumn<SuivieObjectif, Integer> cID;

    @FXML
    private DatePicker date;

    @FXML
    private TextField idsuivi;

    @FXML
    private TextField nouvPoid;

    @FXML
    private ComboBox<Integer> weightComboBox;

    @FXML
    private TableView<SuivieObjectif> tableView;
    ServiceSuiviObj os=new ServiceSuiviObj();
    private ProduitController produitController;
    private CommandeController commandeController;

    @FXML
    private VBox produitsView;
    @FXML
    private Stage stage;



    @FXML
    void objectifback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CRUD.fxml"));
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
    void objectifront(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ADDCRUD.fxml"));
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
    void suivifront(ActionEvent event) {
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
    void suiviback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CRUDSUIVI.fxml"));
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
    void Modifier(ActionEvent event) {
        LocalDate selectedDate = date.getValue();
        // Vérifier si weightComboBox.getValue() est null
        if (weightComboBox.getValue() != null) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);
            SuivieObjectif suivi = new SuivieObjectif(
                    weightComboBox.getValue(),
                    Integer.parseInt(nouvPoid.getText()),
                    sqlDate
            );
            suivi.setId(Integer.parseInt(idsuivi.getText()));
            try {
                os.update(suivi);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("User modifié avec succès!");
                alert.show();

                initialize();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText(e.getMessage());
                alert.show();
                throw new RuntimeException(e);
            }
        } else {
            // Gérer le cas où weightComboBox.getValue() est null
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Veuillez sélectionner une valeur dans weightComboBox.");
            alert.show();
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {
        int id1 = Integer.parseInt(idsuivi.getText());
        try {
            os.delete(id1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("User supprimer avec succés!");
            alert.show();

            initialize();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText(e.getMessage());
            alert.show();
            throw new RuntimeException(e);
        }

    }

    @FXML
    void initialize() throws SQLException {
        ServiceObjectif o = new ServiceObjectif();
        o.chargerweight(weightComboBox);
        ServiceSuiviObj service= new ServiceSuiviObj();
        List<SuivieObjectif> list=service.read();
        ObservableList<SuivieObjectif> ob= FXCollections.observableList(list);
        tableView.setItems(ob);

        cID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Cnouveau.setCellValueFactory(new PropertyValueFactory<>("nouvPoid"));
        Cancien.setCellValueFactory(new PropertyValueFactory<>("obj_id"));
        Cdate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableview();
        produitsView = new VBox();
        try {
            produitController = new ProduitController();
            commandeController = new CommandeController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        stage =new Stage();
    }

    @FXML
    void tableview() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                SuivieObjectif selectedObj = tableView.getSelectionModel().getSelectedItem();
                if (selectedObj != null) {
                    idsuivi.setText(String.valueOf(selectedObj.getId()));
                    weightComboBox.setValue(selectedObj.getObj_id());
                    // Afficher la date dans le champ de texte
                    java.sql.Date sqlDate = (java.sql.Date) selectedObj.getDate();

                    // Convertir java.sql.Date en java.time.LocalDate
                    LocalDate localDate = sqlDate.toLocalDate();

                    // Définir la date dans le DatePicker
                    date.setValue(localDate);
                    nouvPoid.setText(String.valueOf(selectedObj.getNouvPoid()));
                }
            }
        });
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
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewCommande(ActionEvent event) {



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
    void openViewUser(ActionEvent event) {

    }

}
