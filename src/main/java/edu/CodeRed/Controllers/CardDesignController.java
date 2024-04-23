package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CardDesignController implements Initializable {

    @FXML
    private Label Ingredient_name;

    @FXML
    private Button _addBtn;

    @FXML
    private AnchorPane card_form;

    @FXML
    private ImageView prod_imageView;

    @FXML
    void addBtn(ActionEvent event) {

    }

    private Ingredient ingredient;
    private Image image;

    public void setIngredientData(Ingredient ingredient){
        this.ingredient = ingredient;
        Ingredient_name.setText(ingredient.getNom());
        String path = "File:"+ ingredient.getImage();
        image = new Image(path,190,94 ,false,true);
        prod_imageView.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
