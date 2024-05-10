package edu.CodeRed.Controllers;



import edu.CodeRed.entities.Activite;
import edu.CodeRed.entities.Suivi_Activite;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.ActiviteService;
import edu.CodeRed.services.Suivi_ActiviteService;
import edu.CodeRed.tools.MyConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AjouterSuiviControler implements Initializable {

    ActiviteService as = new ActiviteService();
    Suivi_Activite a = new Suivi_Activite();

    Suivi_ActiviteService sa= new Suivi_ActiviteService();

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker tDate;

    @FXML
    private ComboBox<String> comboActivity;

    @FXML
    private TextField tNbrRep;
    private  String nomActivity="";

    @FXML
    void ajoutersuivi(ActionEvent events) {


        a.setDate(Date.valueOf(tDate.getValue()));
        a.setRep(Integer.parseInt(tNbrRep.getText()));
        Preferences p = Preferences.userNodeForPackage(getClass());
        user user= new user();
        p.getInt("userId", user.getId());
        System.out.println(p.getInt("userId", user.getId())+"majd");

        Activite IdActivity = as.readActiviteByName(nomActivity);
        a.setActivite_id(IdActivity);
        //  UserId ajouté dynamiquement selon l'utilisateur connecté
        sa.addSuivi(a,a.getActivite_id().getId(),  p.getInt("userId", user.getId()));
        try {


            Parent loader = FXMLLoader.load(getClass().getResource("/ListeSuivi.fxml"));
            comboActivity.getScene().setRoot(loader);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Activite> listActivity = FXCollections.observableList(as.afficherAllActivite());
        ObservableList<String> listNomActivity =  FXCollections.observableArrayList();
        for (Activite ac:listActivity) {
            listNomActivity.add(ac.getNom());
        }
        comboActivity.setItems(listNomActivity);
        comboActivity.setOnAction(event -> {
            // Récupérer l'option sélectionnée
            nomActivity=comboActivity.getValue();
        });
        LocalDate dateJour = LocalDate.now();

        tDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Désactiver les dates avant la date minimale
                        if (item.isBefore(dateJour)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Vous pouvez personnaliser le style si vous le souhaitez
                        }
                    }
                };
            }
        });


}
}





