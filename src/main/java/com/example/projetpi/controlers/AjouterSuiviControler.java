package com.example.projetpi.controlers;



import com.example.projetpi.entities.Activite;
import com.example.projetpi.entities.Suivi_Activite;
import com.example.projetpi.services.ActiviteService;
import com.example.projetpi.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AjouterSuiviControler implements Initializable {

    ActiviteService as = new ActiviteService();
    MyConnection mc = new MyConnection();
    Suivi_Activite a = new Suivi_Activite();

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker tDate;

    @FXML
    private TextField tIDAct;

    @FXML
    private TextField tNbrRep;

    @FXML
    void ajoutersuivi(ActionEvent event) {
        int activiteId = Integer.parseInt(tIDAct.getText());
        a.setActivite_id(activiteId);
        a.setDate(Date.valueOf(tDate.getValue()));
        a.setRep(Integer.parseInt(tNbrRep.getText()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

