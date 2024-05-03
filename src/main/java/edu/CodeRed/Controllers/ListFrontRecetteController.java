package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ListFrontRecetteController implements Initializable {

    @FXML
    private ScrollPane Rec_ScrollPane;

    @FXML
    private GridPane Recette_gridPane;

    @FXML
    private GridPane Recette_gridPaneSuggest;

    @FXML
    private Pagination pag;

    RecetteService rs = new RecetteService();

    JournalService js = new JournalService();
    List<Recette> recetteList = rs.getAllDataRecette();

    private static int idRecette;

    public int getIdRecette(){
        return this.idRecette;
    }

    public void setIdRecette(int id){
        this.idRecette=id;
        System.out.println("Set Recipe ID: " + idRecette);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Received Recipe ID: " + idRecette);
        try {
            // Get the top 5 most used recettes
            List<Recette> top5Recettes = js.getTop5MostUsedRecettes();

            // Add the top 5 most used recettes to the Recette_gridPaneSuggest
            int columnSuggest = 0;
            int rowSuggest = 1;
            for (int i = 0; i < top5Recettes.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CardDesignRecetteFront.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CardDesignRecetteFrontController itemController1 = fxmlLoader.getController();
                itemController1.setRecetteDataF(top5Recettes.get(i));
                setIdRecette(top5Recettes.get(i).getId());
                System.out.println(getIdRecette());

                if (columnSuggest == 2) {
                    columnSuggest = 0;
                    rowSuggest++;
                }

                // Add space between cards
                anchorPane.setPadding(new Insets(50));

                Recette_gridPaneSuggest.add(anchorPane, columnSuggest++, rowSuggest); //(child,column,row)
                //set grid width
                Recette_gridPaneSuggest.setMinWidth(Region.USE_COMPUTED_SIZE);
                Recette_gridPaneSuggest.setPrefWidth(Region.USE_COMPUTED_SIZE);
                Recette_gridPaneSuggest.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                Recette_gridPaneSuggest.setMinHeight(Region.USE_COMPUTED_SIZE);
                Recette_gridPaneSuggest.setPrefHeight(Region.USE_COMPUTED_SIZE);
                Recette_gridPaneSuggest.setMaxHeight(Region.USE_PREF_SIZE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Calculate the number of pages needed
        int totalPages = (int) Math.ceil(recetteList.size() / 6.0);

        // Set the total number of pages
        pag.setPageCount(totalPages);

        // Set the page factory to dynamically load cards for each page
        pag.setPageFactory(pageIndex -> {
            // Create a new GridPane to hold the cards for the current page
            GridPane pageGridPane = new GridPane();
            pageGridPane.setHgap(10); // Horizontal gap between cards
            pageGridPane.setVgap(10); // Vertical gap between cards
            pageGridPane.setPadding(new Insets(10)); // Padding for the entire page

            // Calculate the starting index and ending index of recipes for the current page
            int startIndex = pageIndex * 6;
            int endIndex = Math.min(startIndex + 6, recetteList.size());

            // Loop through the recipes for the current page
            for (int i = startIndex; i < endIndex; i++) {
                // Load the FXML file for the recipe card
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CardDesignRecetteFront.fxml"));
                AnchorPane anchorPane;
                try {
                    anchorPane = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Get the controller of the loaded FXML
                CardDesignRecetteFrontController itemController = fxmlLoader.getController();

                // Set the recipe data for the current card
                itemController.setRecetteDataF(recetteList.get(i));

                // Set the recipe ID in the controller
                itemController.setIdRecette(recetteList.get(i).getId());

                // Calculate the row and column indices for the current card in the GridPane
                int row = (i - startIndex) / 3; // Three cards per row
                int col = (i - startIndex) % 3;

                // Add the card to the GridPane at the calculated row and column indices
                pageGridPane.add(anchorPane, col, row);
            }

            // Return the GridPane containing the cards for the current page
            return pageGridPane;
        });
    }
}
