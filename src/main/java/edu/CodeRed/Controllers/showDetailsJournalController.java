package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class showDetailsJournalController implements Initializable {

    @FXML
    private Label labelCaloriesJournal;

    @FXML
    private Label labelDateJournal;

    @FXML
    private VBox vBoxRecettes;


    Journal j;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JournalService js=new JournalService();
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/viewJournal.fxml"));
            AnchorPane pane = load.load();
            viewJournalController item = load.getController();

            try {
                j = js.findById(item.getIdJournal());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        labelCaloriesJournal.setText(String.valueOf(j.getCaloriesJournal()));
        labelDateJournal.setText(String.valueOf(j.getDate()));

        List<Recette> recList = new ArrayList<>();
        recList=js.getRecettesForJournal(j.getId());
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


        //////////////////////////////////////////////


        JournalService jc=new JournalService();
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/calendar.fxml"));
            AnchorPane pane = load.load();
            CalendarController item = load.getController();

            try {
                j = js.findById(item.getSelectedJournalId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        labelCaloriesJournal.setText(String.valueOf(j.getCaloriesJournal()));
        labelDateJournal.setText(String.valueOf(j.getDate()));

        List<Recette> recListCalendar = new ArrayList<>();
        recListCalendar=js.getRecettesForJournal(j.getId());
        for(int i=0;i<recListCalendar.size();i++){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/itemRecette.fxml"));
                AnchorPane pane = load.load();
                itemRecetteController item = load.getController();
                item.setData(recListCalendar.get(i));
                vBoxRecettes.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
