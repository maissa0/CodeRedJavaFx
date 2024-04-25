package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.services.RecetteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class viewRecetteController implements Initializable {

    @FXML
    private TableColumn<Recette, Void> actionsColumn;

    @FXML
    private TableColumn<Recette,Integer> calorieColumn;

    @FXML
    private TableColumn<Recette, String> categColumn;

    @FXML
    private TableColumn<Recette, String> descriptionColumn;

    @FXML
    private TableColumn<Recette, String> imgColumn;

    @FXML
    private TableColumn<Recette, String> nomColumn;

    @FXML
    private TableView<Recette> tabRecette;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        RecetteService rs = new RecetteService();
        ObservableList<Recette> list = FXCollections.observableList(rs.getAllDataRecette());

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nomColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recette, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Recette, String> event) {
                Recette a = event.getRowValue();
                a.setNom(event.getNewValue());
                RecetteService as = new RecetteService();
                as.updateRecette(a,a.getIngredients());
            }
        });
        categColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        categColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recette, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Recette, String> event) {
                Recette a = event.getRowValue();
                a.setCategorie(event.getNewValue());
                RecetteService as = new RecetteService();
                as.updateRecette(a,a.getIngredients());
            }
        });
        imgColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imgColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        imgColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recette, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Recette, String> event) {
                Recette a = event.getRowValue();
                a.setImage(event.getNewValue());
                RecetteService as = new RecetteService();
                as.updateRecette(a,a.getIngredients());
            }
        });
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recette, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Recette, String> event) {
                Recette a = event.getRowValue();
                a.setDescription(event.getNewValue());
                RecetteService as = new RecetteService();
                as.updateRecette(a,a.getIngredients());
            }
        });
        calorieColumn.setCellValueFactory(new PropertyValueFactory<>("calorieRecette"));
        calorieColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        calorieColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recette, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Recette, Integer> event) {
                Recette a = event.getRowValue();
                a.setCalorieRecette(event.getNewValue());
                RecetteService as = new RecetteService();
                as.updateRecette(a,a.getIngredients());
            }
        });
        actionsColumn.setCellFactory(createActionsCellFactory());
        tabRecette.setItems(list);
    }


    private static int idRecette;

    public int getIdRecette(){
        return this.idRecette;
    }
    public void setIdRecette(int id){
        this.idRecette=id;
    }

    private Callback<TableColumn<Recette, Void>, TableCell<Recette, Void>> createActionsCellFactory() {
        return new Callback<TableColumn<Recette, Void>, TableCell<Recette, Void>>() {
            @Override
            public TableCell<Recette, Void> call(final TableColumn<Recette, Void> param) {
                return new TableCell<Recette, Void>() {
                    private final Button btnDelete = new Button("Supprimer");
                    private final Button btnDetails = new Button("Details");

                    {
                        btnDelete.setOnAction(event -> handleDelete());
                        btnDetails.setOnAction(event -> {
                            setIdRecette(tabRecette.getSelectionModel().getSelectedItem().getId());
                            System.out.println(getIdRecette());
                            try {
                                openDetails();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Recette currentRec = getTableView().getItems().get(getIndex());
                            if (currentRec != null) {
                                HBox buttonsBox = new HBox(btnDelete,btnDetails);
                                setGraphic(buttonsBox);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };
    }


    void openDetails()throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/showDetailsRecette.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    void handleDelete() {
        Recette selectedRecette = tabRecette.getSelectionModel().getSelectedItem();
        if (selectedRecette != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer cette recette?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the selected item
                RecetteService rs = new RecetteService();
                rs.deleteRecette(selectedRecette.getId());

                ObservableList<Recette> updatedList = FXCollections.observableList(rs.getAllDataRecette());
                tabRecette.setItems(updatedList);
            }
        } else {
            // No item selected, show an information alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un ingrédient à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void open_Stat(ActionEvent event) throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/statistiques.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
