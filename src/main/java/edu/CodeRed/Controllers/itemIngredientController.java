package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class itemIngredientController implements Initializable {

    @FXML
    private Label categIng;

    @FXML
    private ImageView imgIng;

    @FXML
    private Label nomIng;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    Ingredient ingredient;
    void setData(Ingredient ing){
        this.ingredient=ing;
        nomIng.setText(ingredient.getNom());
        categIng.setText(ingredient.getCategorieing());
        System.out.println(ingredient.getImage());
        imgIng.setImage(new Image("file:///C:/tools/pidev-code-red_test/public/img/"+ingredient.getImage()));
    }
}
