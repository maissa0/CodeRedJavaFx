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


    Journal j;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JournalService js = new JournalService();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendar.fxml"));

        try {
            AnchorPane pane = loader.load();
            CalendarController controller = loader.getController();
            int selectedJournalId = controller.getSelectedJournalId();
            j = js.findById(selectedJournalId);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        labelCaloriesJournal.setText(String.valueOf(j.getCaloriesJournal()));
        labelDateJournal.setText(String.valueOf(j.getDate()));

        System.out.println(j.getCaloriesJournal());
        System.out.println(j.getDate());

        List<Recette> recList = new ArrayList<>();
        recList = js.getRecettesForJournal(j.getId());
        for (Recette recette : recList) {
            try {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/itemRecette.fxml"));
                AnchorPane pane = load.load();
                itemRecetteController item = load.getController();
                item.setData(recette);
                vBoxRecettes.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
