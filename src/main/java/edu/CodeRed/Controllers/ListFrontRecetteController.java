package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListFrontRecetteController implements Initializable {

    @FXML
    private ScrollPane Rec_ScrollPane;

    @FXML
    private GridPane Recette_gridPane;

    RecetteService rs = new RecetteService();
    List<Recette> recetteList = rs.getAllDataRecette();

    private static int idRecette;

    public int getIdRecette(){
        return this.idRecette;
    }

    public void setIdRecette(int id){
        this.idRecette=id;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 0;
        int row = 1;
        for (int i = 0; i < recetteList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/CardDesignRecetteFront.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CardDesignRecetteFrontController itemController = fxmlLoader.getController();
            itemController.setRecetteDataF(recetteList.get(i));
            setIdRecette(recetteList.get(i).getId());
            System.out.println(getIdRecette());


            if (column == 3) {
                column = 0;
                row++;
            }

            Recette_gridPane.add(anchorPane, column++, row); //(child,column,row)
            //set grid width
            Recette_gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            Recette_gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            Recette_gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            Recette_gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            Recette_gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            Recette_gridPane.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }

}
