package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class itemRecetteController implements Initializable {

    @FXML
    private Label categRec;

    @FXML
    private ImageView imgRec;

    @FXML
    private Label nomRec;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    Recette recette;
    void setData(Recette rec){
        this.recette=rec;
        nomRec.setText(recette.getNom());
        categRec.setText(recette.getCategorie());
        imgRec.setImage(new Image("file:///C:/tools/optihealth/src/main/java/edu/CodeRed/uploads/"+recette.getImage()));
    }
}
