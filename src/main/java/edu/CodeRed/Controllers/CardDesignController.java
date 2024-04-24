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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardDesignController {

    @FXML
    private Label nameIng;

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
        System.out.println(ingredient.getId());
        nameIng.setText(ingredient.getNom());
        image = new Image("C:\\tools\\optihealth\\src\\main\\resources\\images\\IngredientsImages\\"+ingredient.getImage());
        prod_imageView.setImage(image);
    }


    private static List<Ingredient> listIngToRecette = new ArrayList<>();

    List<Ingredient> getListIngToRecette() {
        return this.listIngToRecette;
    }

    @FXML
    void ajouterIngToRecette(ActionEvent event) {
        listIngToRecette.add(ingredient);
    }




}
