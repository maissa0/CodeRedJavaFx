package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Suivi_Activite;
import edu.CodeRed.services.Suivi_ActiviteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ListeSuiviControler implements Initializable {

        @FXML
        private TableColumn<Suivi_Activite,Integer> Labe;

        @FXML
        private Button btnAjoutSuivi;

        @FXML
        private Button btnModifier;

        @FXML
        private Button btnSupprimer;

        @FXML
        private TableColumn<Suivi_Activite, Date> colDate;

        @FXML
        private TableColumn<Suivi_Activite, Integer> colIDActi;
        @FXML
        private TableView<Suivi_Activite> TableSuivi;

        @FXML
        void AjouterSuivi(ActionEvent event) {


                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSuivi.fxml"));
                        Parent root = loader.load();
                        Scene currentScene = ((Button) event.getSource()).getScene();
                        Stage stage = (Stage) currentScene.getWindow();
                        Scene newScene = new Scene(root);
                        stage.setScene(newScene);
                        stage.show();
                } catch (IOException e) {

                        e.printStackTrace();
                }
        }

        @FXML
        void ModifierSuivi(ActionEvent event) {

        }

        @FXML
        void SupprimerSuivi(ActionEvent event) {

        }
        Suivi_ActiviteService sa= new Suivi_ActiviteService();

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                ObservableList<Suivi_Activite> list = FXCollections.observableList(sa.afficherAllSuivi());
                colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                colIDActi.setCellValueFactory(new PropertyValueFactory<>("activite_id"));
                Labe.setCellValueFactory(new PropertyValueFactory<>("rep"));

                TableSuivi.getColumns().addAll(colDate, colIDActi, Labe);
                TableSuivi.setItems(list);
        }



}
