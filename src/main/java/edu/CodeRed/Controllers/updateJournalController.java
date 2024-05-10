package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class updateJournalController implements Initializable {

    @FXML
    private ScrollPane Rec_ScrollPane;

    @FXML
    private TableColumn<Recette, String> categRecCol;

    @FXML
    private GridPane ingredient_gridPane;

    @FXML
    private TableView<Recette> listRec;

    @FXML
    private TableColumn<Recette, String> nomRecCol;

    @FXML
    private DatePicker txtDateJournal;

    RecetteService rs = new RecetteService();
    JournalService js = new JournalService();
    List<Recette> recetteList = rs.getAllDataRecette();


    Journal journal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 0;
        int row = 1;
        for (int i = 0; i < recetteList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/CardDesignRecette.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CardDesignRecetteController itemController = fxmlLoader.getController();
            itemController.setRecetteData(recetteList.get(i));


            if (column == 3) {
                column = 0;
                row++;
            }

            ingredient_gridPane.add(anchorPane, column++, row); //(child,column,row)
            //set grid width
            ingredient_gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            ingredient_gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }

    @FXML
    void updateJournal(ActionEvent event) {
        if(txtDateJournal.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez saisir la date de votre journal.");
            Optional<ButtonType> option = alert.showAndWait();
        } else {
            modifierJournal();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Journal a été modifié avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    public ObservableList<Recette> CardListDataRecette = FXCollections.observableArrayList();
    void modifierJournal(){


        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/viewJournal.fxml"));
            AnchorPane pane = load.load();
            viewJournalController item = load.getController();
            journal = js.findById(item.getIdJournal());
            if(journal == null){
                System.out.println("erreur journal");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Recette> recList;
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/CardDesignRecette.fxml"));
            AnchorPane pane = load.load();
            CardDesignRecetteController item = load.getController();
            recList = js.getRecettesForJournal(journal.getId());
            if(recList.isEmpty()){
                System.out.println("erreur");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!recList.isEmpty()){
            for(int i=0;i<recList.size();i++){
                CardListDataRecette.add(recList.get(i));
            }
            nomRecCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            categRecCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            listRec.setItems(CardListDataRecette);
        }

        Date dateJournal=null;
        try {
            LocalDate localDate = txtDateJournal.getValue();
            if (localDate != null) {
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                dateJournal = Date.from(instant);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        int id_user=1;

        JournalService js = new JournalService();
        Journal j = new Journal(journal.getId(),
                id_user,dateJournal);
        js.updateJournal(j,recList);
    }
}
