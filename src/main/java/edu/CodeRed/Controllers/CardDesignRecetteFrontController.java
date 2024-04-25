package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    void showDetailsRecetteF(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/showDetailsRecetteFront.fxml"));
        Parent root = loader.load();
        showDetailsRecetteFrontController controller = loader.getController();

        // Pass the ID of the recipe to the showDetailsRecetteFrontController
        controller.setIdRecette(rec.getId()); // Assuming rec is the selected recipe

        // Close the current stage
        Stage stage = (Stage) card_form.getScene().getWindow();
        stage.close();

        // Open a new stage to show the details of the recipe
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }

    private Recette rec;
    private Image image;

    public void setRecetteDataF(Recette rec){
        this.rec = rec;
        System.out.println(rec.getId());
        nameRecF.setText(rec.getNom());
        categRecF.setText(rec.getCategorie());
        image = new Image("C:\\tools\\optihealth\\src\\main\\java\\edu\\CodeRed\\uploads\\"+rec.getImage());
        prod_imageView.setImage(image);
    }



}
