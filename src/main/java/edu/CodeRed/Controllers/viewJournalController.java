package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class viewJournalController implements Initializable {

    @FXML
    private TableColumn<Journal, Void> actionsColumn;

    @FXML
    private TableColumn<Journal, Integer> caloriesColumn;

    @FXML
    private TableColumn<Journal, Date> dateColumn;

    @FXML
    private TableView<Journal> tabJournal;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JournalService js = new JournalService();
        ObservableList<Journal> list = FXCollections.observableList(js.getAllDataJournal());

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public String toString(Date object) {
                return dateFormat.format(object);
            }

            @Override
            public Date fromString(String string) {
                try {
                    // Parse the string into a Date object using the defined format
                    return dateFormat.parse(string);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // If the string can't be parsed, return null
                    return null;
                }
            }
        }));
        dateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Journal, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Journal, Date> event) {
                Journal p = event.getRowValue();
                p.setDate(event.getNewValue());
                JournalService ps = new JournalService();
                ps.updateJournal(p,p.getRecettes());
            }
        });
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesJournal"));
        actionsColumn.setCellFactory(createActionsCellFactory());
        tabJournal.setItems(list);
    }

    private static int idJournal;

    public int getIdJournal(){
        return this.idJournal;
    }
    public void setIdJournal(int id){
        this.idJournal=id;
    }

    private Callback<TableColumn<Journal, Void>, TableCell<Journal, Void>> createActionsCellFactory() {
        return new Callback<TableColumn<Journal, Void>, TableCell<Journal, Void>>() {
            @Override
            public TableCell<Journal, Void> call(final TableColumn<Journal, Void> param) {
                return new TableCell<Journal, Void>() {
                    private final Button btnDelete = new Button("Supprimer");
                    private final Button btnDetails = new Button("Details");

                    private final Button btnUpdate = new Button("Update");
                    {
                        btnDelete.setOnAction(event -> handleDelete());
                        btnDetails.setOnAction(event -> {
                            setIdJournal(tabJournal.getSelectionModel().getSelectedItem().getId());
                            System.out.println(getIdJournal());
                            try {
                                openDetails();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        btnUpdate.setOnAction(event -> {
                            setIdJournal(tabJournal.getSelectionModel().getSelectedItem().getId());
                            System.out.println(getIdJournal());
                            try {
                                openUpdate();
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
                            Journal currentJournal = getTableView().getItems().get(getIndex());
                            if (currentJournal != null) {
                                HBox buttonsBox = new HBox(btnDelete,btnDetails,btnUpdate);
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

    void openUpdate()throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/updateJournal.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }
    void openDetails()throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("/showDetailsJournal.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

    void handleDelete() {
        Journal selectedJournal = tabJournal.getSelectionModel().getSelectedItem();
        if (selectedJournal != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer ce Journal?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the selected item
                JournalService js = new JournalService();
                js.deleteJournal(selectedJournal.getId());

                ObservableList<Journal> updatedList = FXCollections.observableList(js.getAllDataJournal());
                tabJournal.setItems(updatedList);
            }
        } else {
            // No item selected, show an information alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un journal à supprimer.");
            alert.showAndWait();
        }
    }
}
