package controllers;

import entities.Objectif;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import services.ServiceObjectif;
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
    private PieChart pieChart;


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

        pieChart.getData().clear();
        for (String choix : choixList) {
            int occurrences = choixOccurrences.get(choix);
            PieChart.Data data = new PieChart.Data(choix, occurrences);
            pieChart.getData().add(data);
        }

        pieChart.setTitle("Répartition des choix");
    }



}
