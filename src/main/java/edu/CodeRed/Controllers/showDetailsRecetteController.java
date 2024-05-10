package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    private Label labelDescriptionRecette;

    @FXML
    private ImageView labelImgRecette;

    @FXML
    private Label labelNomRecette;

    @FXML
    private VBox vBoxIng;
    @FXML
    private Label Nom;

    @FXML
    private MenuItem gesIng;

    @FXML
    private MenuItem gesIng1;

    @FXML
    private MenuItem gesIng11;

    @FXML
    private MenuItem getRec;

    @FXML
    private MenuItem getRec1;

    @FXML
    private MenuItem getRec11;

    @FXML
    private ImageView picture;

    @FXML
    private ProgressBar progbarb;

    @FXML
    private Label role;

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
        //labelCategRecette.setText(r.getCategorie());
        labelImgRecette.setImage(new Image("C:\\tools\\optihealth\\src\\main\\java\\edu\\CodeRed\\uploads\\"+r.getImage()));
        labelDescriptionRecette.setText(r.getDescription());
        labelCalorieRecette.setText(String.valueOf(r.getCalorieRecette()));
        System.out.println(r.getId());

        if ("Facile".equals(r.getCategorie())) {
            progbarb.setProgress(0.3);

            // Apply custom style to change the color to green
            progbarb.setStyle("-fx-accent: green;");

        }
        else if ("Moyenne".equals(r.getCategorie())){

            progbarb.setProgress(0.5);

            // Apply custom style to change the color to green
            progbarb.setStyle("-fx-accent: orange;");


        }
        else{
            progbarb.setProgress(1);

            // Apply custom style to change the color to green
            progbarb.setStyle("-fx-accent: red;");

        }

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




    @FXML
    void back_to_login(ActionEvent event) {

    }

    @FXML
    void openViewActitvite(ActionEvent event) {

    }

    @FXML
    void openViewCommande(ActionEvent event) {

    }

    @FXML
    void openViewIngredient(ActionEvent event) {

    }

    @FXML
    void openViewObjectif(ActionEvent event) {

    }

    @FXML
    void openViewProduit(ActionEvent event) {

    }

    @FXML
    void openViewRecette(ActionEvent event) {

    }

    @FXML
    void openViewSuiviObj(ActionEvent event) {

    }

    @FXML
    void openViewUser(ActionEvent event) {

    }


    }



