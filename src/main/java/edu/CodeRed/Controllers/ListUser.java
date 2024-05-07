package edu.CodeRed.controllers;

import edu.CodeRed.entities.user;
import edu.CodeRed.services.userservice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;


import java.util.List;
import java.util.Optional;
public class ListUser {



    @FXML
    private ComboBox<String> trilist;

    @FXML
    public TableColumn<user, Void> actions1;

    @FXML
    private TableView<user> list_user;

    @FXML
    public TextField searchbar_id;

    @FXML
    private TableColumn<user, String> user_Role;

    @FXML
    private TableColumn<user, String> user_adresse;

    @FXML
    private TableColumn<user, String> user_datedenaissance;

    @FXML
    private TableColumn<user, String> user_email;

    @FXML
    private TableColumn<user, String> user_genre;

    @FXML
    private TableColumn<user, Integer> user_id;

    @FXML
    private TableColumn<user, String> user_mdp;

    @FXML
    private TableColumn<user, String> user_nom;

    @FXML
    private TableColumn<user, String> user_numtel;

    @FXML
    private TableColumn<user, String > user_prenom;


    private final userservice userService = new userservice(); // Initialize UserService

    //Redirect to Add user
    @FXML
    void      add_user(ActionEvent event) throws IOException {
        Home.loadFXML("/addUser.fxml");
    }

    @FXML
    void initialize() {
        // Call the getAllUserData() method using the instance variable
        ObservableList<user> userList = FXCollections.observableList(userService.getalluserdata());

        // Set cell value factories for each TableColumn
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());
        // Set the items to the TableView
        list_user.setItems(userList);



        trilist.setOnAction(event -> triList(event));
        //search();


        ObservableList<String> Trichoices = FXCollections.observableArrayList("Nom", "Categorie");
        trilist.setItems(Trichoices);

        searchbar_id.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });



    }

    private Callback<TableColumn<user, Void>, TableCell<user, Void>> createActionsCellFactory() {
        return new Callback<TableColumn<user, Void>, TableCell<user, Void>>() {
            @Override
            public TableCell<user, Void> call(final TableColumn<user, Void> param) {
                return new TableCell<user, Void>() {
                    private final Button btnUpdate = new Button("Update");
                    private final Button btnDelete = new Button("Delete");

                    {
                        // Action for the update button
                        btnUpdate.setOnAction(event -> handleUpdate());
                        btnDelete.setOnAction(event -> handleDelete());
                    }

                    // This method is called whenever the cell needs to be updated
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Set the buttons in the cell
                            HBox buttons = new HBox(btnUpdate, btnDelete);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
    }
    @FXML
    void handleUpdate() {
        user selectedProjet = list_user.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            // Load the update scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent updateScene;
            try {
                updateScene = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the controller for the update scene
            UpdateUser updateController = loader.getController();
            updateController.setUser(selectedProjet);

            // Create a new stage for the update scene
            Stage updateStage = new Stage();
            updateStage.setTitle("Update User");
            updateStage.setScene(new Scene(updateScene));

            // Show the update stage
            updateStage.showAndWait();

            // After the update scene is closed, refresh the table view
            refreshList();
        } else {
            // No item selected, show an information alert
            showAlert(Alert.AlertType.INFORMATION, "Information", null, "Veuillez sélectionner un projet à mettre à jour.");
        }
    }

    @FXML
    void handleDelete() {
        user selectedProjet = list_user.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer ce projet?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the selected item
                userService.DeleteEntityWithConfirmation(selectedProjet);
                refreshList(); // Refresh the TableView
            }
        } else {
            // No item selected, show an information alert
            showAlert(Alert.AlertType.INFORMATION, "Information", null, "Veuillez sélectionner un projet à supprimer.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // Method to refresh the list of users
    @FXML
    public void refreshList() {
        ObservableList<user> updatedList = FXCollections.observableList(userService.getalluserdata());
        list_user.setItems(updatedList);
    }



    private void TrieNom() {
        userservice is = new userservice();
        List<user> i = is.triNom();
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());

        list_user.setItems(FXCollections.observableList(i));

    }

    private void Trieemail() {
        userservice is = new userservice();
        List<user> i = is.trimail();
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());


        list_user.setItems(FXCollections.observableList(i));

    }


    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            list_user.setItems(initialData());
        } else {
            ObservableList<user> filteredList = FXCollections.observableArrayList();
            for (user vehicule : list_user.getItems()) {
                if (vehicule.getNom().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(vehicule);
                }
            }
            list_user.setItems(filteredList);
        }
    }

    ObservableList<user>initialData(){
        userservice V = new userservice();
        return  FXCollections.observableArrayList(V.getalluserdata());
    }
    @FXML
    void triList(ActionEvent event) {

        if (trilist.getValue().equals("Nom")) {
            TrieNom();
        } else if (trilist.getValue().equals("email")) {
            Trieemail();
        }
    }

}
