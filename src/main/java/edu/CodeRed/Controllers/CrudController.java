package edu.CodeRed.Controllers;

 import edu.CodeRed.entities.Commande;
 import edu.CodeRed.entities.Objectif;
 import edu.CodeRed.entities.user;
 import edu.CodeRed.services.ServiceObjectif;
 import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
 import javafx.scene.Node;
 import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 import java.util.prefs.Preferences;

public class CrudController {

    @FXML
    private TableColumn<?, ?> ColActivity_level;

    @FXML
    private TableColumn<?, ?> ColAge;

    @FXML
    private TableColumn<?, ?> ColCalorie;

    @FXML
    private TableColumn<?, ?> ColHeight;

    @FXML
    private TableColumn<?, ?> ColID;

    @FXML
    private TableColumn<?, ?> ColObjectif;

    @FXML
    private TableColumn<?, ?> ColSexe;

    @FXML
    private TableColumn<?, ?> ColWeight;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private ComboBox<String> tActivity_level;

    @FXML
    private TextField tAge;
    @FXML
    private TextField  searchField;

    @FXML
    private TextField tCalorie;

    @FXML
    private TextField tHeight;

    @FXML
    private TextField tID;

    @FXML
    private ComboBox<String> tObjectif;

    @FXML
    private ComboBox<String> tSexe;

    @FXML
    private TextField tWeight;

    @FXML
    private TableView<Objectif> tableview;
    @FXML
    private Button ajouterobjectif;
    @FXML
    private Pagination pagination;
    @FXML
    private PieChart pieChart1;



    private ProduitController produitController;
    private CommandeController commandeController;
    TableView<Commande> tableView = new TableView<>();
    @FXML
    private VBox produitsView;
    @FXML
    private Stage stage;

    private ObservableList<Objectif> allData;

    ServiceObjectif os = new ServiceObjectif();

    @FXML
    void Ajouter(ActionEvent event) {

        Objectif o =new Objectif(
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
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText(e.getMessage());
            alert.show();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Modifier(ActionEvent event) {

        Objectif o = new Objectif(

                tSexe.getValue(),
                Integer.parseInt(tAge.getText())
                ,Integer.parseInt(tHeight.getText())
                ,Integer.parseInt(tWeight.getText()),
                tActivity_level.getValue(),
                tObjectif.getValue(),
                Integer.parseInt(tCalorie.getText()));
        o.setId(Integer.parseInt(tID.getText()));
        try {
            os.update(o);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("User modifier avec succés!");
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
    void Supprimer(ActionEvent event) {
        int id1 = Integer.parseInt(tID.getText());
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



    // Dans votre méthode initialize
    @FXML
    void initialize() throws SQLException {

        String[] items = { "Homme", "Femme"};
        tSexe.getItems().addAll(items);
        String[] items2 = { "Sédentaire ", "Léger ","Modéré ","Actif ","Très_actif"};
        tActivity_level.getItems().addAll(items2);
        String[] items3 = { "perdre_de_poids", "gagne_de_poids"};
        tObjectif.getItems().addAll(items3);
        // Votre code d'initialisation existant ici...

        try {
            ObservableList<Objectif> observableliste = FXCollections.observableList(os.read());
            allData = observableliste; // Copiez les données initiales dans la liste allData

            // Remplir les colonnes avec les propriétés des objets User
            ColID.setCellValueFactory(new PropertyValueFactory<>("id"));
            ColSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
            ColAge.setCellValueFactory(new PropertyValueFactory<>("age"));
            ColHeight.setCellValueFactory(new PropertyValueFactory<>("height"));
            ColWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
            ColActivity_level.setCellValueFactory(new PropertyValueFactory<>("activity_level"));
            ColObjectif.setCellValueFactory(new PropertyValueFactory<>("choix"));
            ColCalorie.setCellValueFactory(new PropertyValueFactory<>("calorie"));

            // Configurer la gestion des événements de clic sur la TableView
            tableview();

            int pageCount = (int) Math.ceil((double) observableliste.size() / 3);
            pagination.setPageCount(pageCount);
            pagination.setCurrentPageIndex(0);

            pagination.setPageFactory(pageIndex -> {
                int fromIndex = pageIndex * 3;
                int toIndex = Math.min(fromIndex + 3, observableliste.size());
                tableview.setItems(FXCollections.observableArrayList(observableliste.subList(fromIndex, toIndex)));
                return tableview;
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ajoutez la méthode de recherche pour filtrer les données en fonction du texte saisi
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<Objectif> filteredData = new FilteredList<>(allData, p -> true);

            filteredData.setPredicate(obj -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(obj.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID
                } else if (obj.getSexe().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches sexe
                } else if (String.valueOf(obj.getAge()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches age
                }
                // Add other fields to search if needed...

                return false; // Does not match any filter
            });

            SortedList<Objectif> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableview.comparatorProperty());

            tableview.setItems(sortedData);
        });

        // Appeler la méthode de recherche une seule fois au démarrage de l'application
        search();


        produitsView = new VBox();
        try {
            produitController = new ProduitController();
            commandeController = new CommandeController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        stage =new Stage();
    }




    // Méthode pour configurer un TextField pour accepter uniquement des valeurs numériques
    private void configureNumericTextField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null,
                change -> (change.getControlNewText().matches("\\d*")) ? change : null));
    }


    @FXML
    void tableview() {
        tableview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Objectif selectedObj = tableview.getSelectionModel().getSelectedItem();
                if (selectedObj != null) {
                    tID.setText(String.valueOf(selectedObj.getId()));
                    tSexe.setValue(selectedObj.getSexe());
                    tAge.setText(String.valueOf(selectedObj.getAge()));
                    tHeight.setText(String.valueOf(selectedObj.getHeight()));
                    tWeight.setText(String.valueOf(selectedObj.getWeight()));
                    tActivity_level.setValue(String.valueOf(selectedObj.getActivity_level()));
                    tObjectif.setValue(selectedObj.getChoix());
                    tCalorie.setText(String.valueOf(selectedObj.getCalorie()));
                }
            }
        });
    }
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


    private void search() {
        FilteredList<Objectif> filteredData = new FilteredList<>(allData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(obj -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(obj.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID
                } else if (obj.getSexe().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches sexe
                } else if (String.valueOf(obj.getAge()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches age
                }
                // Add other fields to search if needed...

                return false; // Does not match any filter
            });
        });

        SortedList<Objectif> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);
    }

    @FXML
    void afficherStatistiques(ActionEvent event) {
        // Calculate statistics and update the PieChart
        List<String> choixList = new ArrayList<>();
        Map<String, Integer> choixOccurrences = new HashMap<>();
        for (Objectif objectif : allData) {
            String choix = objectif.getChoix();
            if (!choixList.contains(choix)) {
                choixList.add(choix);
                choixOccurrences.put(choix, 0);
            }
            choixOccurrences.put(choix, choixOccurrences.get(choix) + 1);
        }

        pieChart1.getData().clear();
        for (String choix : choixList) {
            int occurrences = choixOccurrences.get(choix);
            PieChart.Data data = new PieChart.Data(choix, occurrences);
            pieChart1.getData().add(data);
        }

        pieChart1.setTitle("Répartition des choix");
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
        currentStage.close();

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
    void openViewUser(ActionEvent event) {

    }

}
