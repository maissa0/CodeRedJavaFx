package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CardDesignRecetteFrontController {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label categRecF;

    @FXML
    private Label nameRecF;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private ProgressBar progbar;



    @FXML
    void showDetailsRecetteF(ActionEvent event) throws IOException {
        System.out.println("Selected Recipe ID: " + rec.getId());

        // Set the recipe ID in the controller before loading the new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsRecetteFront.fxml"));
        Parent root = loader.load();
        showDetailsRecetteFrontController controller = loader.getController();
        controller.setIdRecette(rec.getId()); // Set the recipe ID here

        // Open a new stage to show the details of the recipe
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }

    private Recette rec;
    private Image image;
    private Image imagebar;

    private static int idRecette;

    public int getIdRecette(){
        return this.idRecette;
    }

    public void setIdRecette(int id){
        this.idRecette=id;
        System.out.println("Set Recipe ID: " + idRecette);
    }

    public void setRecetteDataF(Recette rec) {
        this.rec = rec;
        System.out.println(rec.getId());
        nameRecF.setText(rec.getNom());
        categRecF.setText(rec.getCategorie());
        if ("Facile".equals(rec.getCategorie())) {
            progbar.setProgress(0.3);

            // Apply custom style to change the color to green
            progbar.setStyle("-fx-accent: green;");

        }
        else if ("Moyenne".equals(rec.getCategorie())){

            progbar.setProgress(0.5);

            // Apply custom style to change the color to green
            progbar.setStyle("-fx-accent: orange;");


        }
        else{
            progbar.setProgress(1);

            // Apply custom style to change the color to green
            progbar.setStyle("-fx-accent: red;");

        }
        image = new Image("C:\\tools\\optihealth\\src\\main\\java\\edu\\CodeRed\\uploads\\" + rec.getImage());
        prod_imageView.setImage(image);


    }
}
