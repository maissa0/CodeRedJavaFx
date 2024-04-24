package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class showDetailsRecetteController implements Initializable {

    @FXML
    private Label labelCalorieRecette;

    @FXML
    private Label labelCategRecette;

    @FXML
    private Label labelDescriptionRecette;

    @FXML
    private ImageView labelImgRecette;

    @FXML
    private Label labelNomRecette;

    @FXML
    private VBox vBoxIng;

    Recette r;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RecetteService rs=new RecetteService();
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/viewRecette.fxml"));
            AnchorPane pane = load.load();
            viewRecetteController item = load.getController();

            try {
                r = rs.findById(item.getIdRecette());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        labelNomRecette.setText(r.getNom());
        labelCategRecette.setText(r.getCategorie());
        labelImgRecette.setImage(new Image("C:\\tools\\optihealth\\src\\main\\java\\edu\\CodeRed\\uploads\\"+r.getImage()));
        labelDescriptionRecette.setText(r.getDescription());
        labelCalorieRecette.setText(String.valueOf(r.getCalorieRecette()));
        System.out.println(r.getId());

        List<Ingredient> ingList = new ArrayList<>();
        ingList=rs.getIngredientsForRecette(r.getId());
        for(int i=0;i<ingList.size();i++){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/itemIngredient.fxml"));
                AnchorPane pane = load.load();
                itemIngredientController item = load.getController();
                item.setData(ingList.get(i));
                vBoxIng.getChildren().add(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
