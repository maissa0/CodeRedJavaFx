package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.tools.MyConnexion;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddRecetteController {

    @FXML
    private ScrollPane Ingredient_ScrollPane;

    @FXML
    private TextField calRec;

    @FXML
    private ComboBox<String> catRec;

    @FXML
    private TextArea descRec;

    @FXML
    private TextField imgRec;

    @FXML
    private TableView<Ingredient> listIngr;

    @FXML
    private TextField nomRec;

    @FXML
    private Button rafraichiring;

    @FXML
    private GridPane ingredient_gridPane;

    public ObservableList<Ingredient> CardListData = FXCollections.observableArrayList();

    @FXML
    void rafraichirRec(ActionEvent event) {
    }

    @FXML
    void uploadRec(ActionEvent event) {
    }

    public ObservableList<Ingredient> ingredientGetData() {

        String requette = "SELECT * FROM ingredient";
        ObservableList<Ingredient> listData = FXCollections.observableArrayList();

        try {
            Statement st = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requette);
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setNom(rs.getString("nom"));
                ingredient.setImage(rs.getString("image"));

                listData.add(ingredient);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return listData;
    }

    public void ingDisplayCard() {
        CardListData.clear();
        CardListData.addAll(ingredientGetData());
        int row = 0;
        int column = 0;

        for (int q = 0; q < CardListData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("CardDesign.fxml"));
                AnchorPane pane = load.load();
                CardDesignController cardC = load.getController();
                cardC.setIngredientData(CardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                ingredient_gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void initialize() {
        ingDisplayCard();
    }
}
