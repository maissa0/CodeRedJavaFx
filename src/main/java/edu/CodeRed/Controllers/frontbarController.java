package edu.CodeRed.Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class frontbarController {

    @FXML
    void objectiffront(ActionEvent event) {

    }

    @FXML
    void openViewCommand(ActionEvent event) {

    }

    @FXML
    void openViewJournal(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewJournal.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void openViewPanier(ActionEvent event) {

    }

    @FXML
    void openViewProduct(ActionEvent event) {

    }

    @FXML
    void openViewRecettes(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void openViewSuivAct(ActionEvent event) {

    }

    @FXML
    void openViewSuivObj(ActionEvent event) {

    }

}
