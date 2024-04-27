package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.services.RecetteService;
import edu.CodeRed.tools.MyConnexion;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class AddRecetteController implements Initializable {

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
    private GridPane ingredient_gridPane;

    @FXML
    private TableView<Ingredient> listIngr;
    @FXML
    private TableColumn<Ingredient, String> categIngCol;

    @FXML
    private TableColumn<Ingredient, String> nomIngCol;

    @FXML
    private TextField nomRec;

    @FXML
    private ImageView imgRecetteInput;

    @FXML
    private Button rafraichiring;



    IngredientService is = new IngredientService();
    List<Ingredient> ingredients = is.getAllData();;


    public ObservableList<Ingredient> CardListData = FXCollections.observableArrayList();

    @FXML
    void rafraichirRec(ActionEvent event) {

    }

    private File selectedImageFile;
    private String imageName = null ;

    @FXML
    void uploadRec(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(imgRecetteInput.getScene().getWindow());
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imgRecetteInput.setImage(image);

            // Générer un nom de fichier unique pour l'image
            String uniqueID = UUID.randomUUID().toString();
            String extension = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
            imageName = uniqueID + extension;

            Path destination = Paths.get(System.getProperty("user.dir"), "src","main","java","edu","CodeRed", "uploads", imageName);
            Files.copy(selectedImageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        }
    }

    @FXML
    void addRecette(ActionEvent event) {
        if (nomRec.getText().isEmpty() || catRec.getItems().isEmpty() || descRec.getText().isEmpty() || calRec.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les détails concernant votre recette.");
            alert.showAndWait();
        } else {
            int cal = 0;
            try {
                cal = Integer.parseInt(calRec.getText());
                if (cal <= 0) {
                    // Notify the user about the invalid input
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Valeur invalide");
                    alert.setHeaderText(null);
                    alert.setContentText("La valeur des calories doit être un entier positif.");
                    alert.showAndWait();
                    return; // Stop further execution
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                // Notify the user about the invalid input
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Valeur invalide");
                alert.setHeaderText(null);
                alert.setContentText("La valeur des calories doit être un entier positif.");
                alert.showAndWait();
                return; // Stop further execution
            }

            ajouterRecette();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ajouté avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre recette a été ajoutée avec succès.");
            alert.showAndWait();
        }
    }

    void ajouterRecette(){

        List<Ingredient> ingredientList;
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/CardDesign.fxml"));
            AnchorPane pane = load.load();
            CardDesignController item = load.getController();
            ingredientList = item.getListIngToRecette();
            if(ingredientList.isEmpty()){
                System.out.println("erreur");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!ingredientList.isEmpty()){
            for(int i=0;i<ingredientList.size();i++){
                CardListData.add(ingredientList.get(i));
            }
            nomIngCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            categIngCol.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
            listIngr.setItems(CardListData);
        }

        String nom=nomRec.getText();
        String description=descRec.getText();
        String categ=catRec.getValue();
        String image=imageName;
        int cal = Integer.parseInt(calRec.getText());


        RecetteService rs = new RecetteService();
        Recette r = new Recette(nom,categ,image,description,cal);
        rs.addRecette(r,ingredientList);
    }

    public List<Ingredient> ingredientGetData() {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM ingredient";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setNom(rs.getString("nom"));
                    ingredient.setImage(rs.getString("image"));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> options = FXCollections.observableArrayList(
                "Facile","Moyenne","Difficile");
        catRec.setItems(options);
        int column = 0;
        int row = 1;
        for (int i = 0; i < ingredients.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/CardDesign.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CardDesignController itemController = fxmlLoader.getController();
            itemController.setIngredientData(ingredients.get(i));


            if (column == 3) {
                column = 0;
                row++;
            }

            ingredient_gridPane.add(anchorPane, column++, row); //(child,column,row)
            //set grid width
            ingredient_gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            //set grid height
            ingredient_gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            ingredient_gridPane.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }
}


    

