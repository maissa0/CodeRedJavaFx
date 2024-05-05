package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class showDetailsJournalCalController implements Initializable {

    @FXML
    private Label labelCaloriesJournal;

    @FXML
    private Label labelDateJournal;

    @FXML
    private VBox vBoxRecettes;

    private int selectedJournalId;



    Journal j;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Retrieve the selected journal by ID
            Journal journal = JournalService.findById1(selectedJournalId);
            System.out.println(selectedJournalId);
            if (journal != null) {
                // Set label texts with journal information
                labelCaloriesJournal.setText(String.valueOf(journal.getCaloriesJournal()));
                labelDateJournal.setText(String.valueOf(journal.getDate()));

                // Retrieve associated recettes for the journal
                List<Recette> recettes = JournalService.getRecettesForJournal(journal.getId());
                for (Recette recette : recettes) {
                    try {
                        // Load and display each recette in the VBox
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/itemRecette.fxml"));
                        AnchorPane pane = loader.load();
                        itemRecetteController item = loader.getController();
                        item.setData(recette);
                        vBoxRecettes.getChildren().add(pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
