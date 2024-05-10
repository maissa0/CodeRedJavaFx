package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardDesignRecetteController implements Initializable {

    @FXML
    private Label nameRec;

    @FXML
    private Label calgRec;

    @FXML
    private Button _addBtn;

    @FXML
    private AnchorPane card_form;

    @FXML
    private ImageView prod_imageView;

    @FXML
    void addBtn(ActionEvent event) {

    }

    private Recette rec;
    private Image image;

    public void setRecetteData(Recette rec){
        this.rec = rec;
        System.out.println(rec.getId());
        nameRec.setText(rec.getNom());
        calgRec.setText(String.valueOf(rec.getCalorieRecette()));
        image = new Image("C:\\tools\\optihealth\\src\\main\\java\\edu\\CodeRed\\uploads\\"+rec.getImage());
        prod_imageView.setImage(image);
    }


    private static List<Recette> listRecToJournal = new ArrayList<>();

    List<Recette> getListRecToJournal() {
        return this.listRecToJournal;
    }

    @FXML
    void ajouterRecToJournal(ActionEvent event) {
        listRecToJournal.add(rec);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
