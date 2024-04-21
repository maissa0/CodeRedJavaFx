package edu.CodeRed.Controllers;

import com.mysql.cj.xdevapi.JsonParser;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.services.IngredientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;


public class IngredientController {

    private Ingredient ingredient;

    @FXML
    private Button rafraichiring;

    @FXML
    private TableColumn<Ingredient, Void> actions; // Modifier le type générique

    @FXML
    private TableColumn<Ingredient, String> IngredientCategorie; // Modifier le type générique

    @FXML
    private TableColumn<Ingredient, String> IngredientImage; // Modifier le type générique

    @FXML
    private Button ajouterIng;

    @FXML
    private Label categorie;

    @FXML
    private ComboBox<String> categorieIng;

    @FXML
    private TextField imageIng;

    @FXML
    private Button importerIng;

    @FXML
    private TableColumn<Ingredient, String> ingredientNom; // Modifier le type générique

    @FXML
    private TableView<Ingredient> listing;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button modifierIng;

    @FXML
    private TextField searchbar_id;

    @FXML
    private TextField nomIng;

    @FXML
    private Button viderIng;


    @FXML
    void initialize() {
        ObservableList<String> categoriesOptions = FXCollections.observableArrayList("cereales", "sucreries", "viandes", "legumes", "fruits", "produit laitiers");
        categorieIng.setItems(categoriesOptions);

        IngredientService ingredientService = new IngredientService();
        ObservableList<Ingredient> list = FXCollections.observableList(ingredientService.getAllData());

        ingredientNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        IngredientCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
        IngredientImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        actions.setCellFactory(createActionsCellFactory());
        listing.setItems(list);
    }


    @FXML
    void AjouterIngredient(ActionEvent event) {

        String picturePath = imageIng.getText();
        Path path = Paths.get(picturePath);
        String fileName = path.getFileName().toString();
        System.out.println(fileName);

        try {
            // Vérifier que tous les champs ne sont pas null
            if (nomIng.getText() == null || categorieIng.getValue() == null || imageIng.getText() == null) {
                showAlert("Veuillez remplir tous les champs.");
                return;
            }

            // Vérifier que tous les champs sont remplis
            if (nomIng.getText().isEmpty() || categorieIng.getValue().isEmpty() || imageIng.getText().isEmpty()) {
                showAlert("Veuillez remplir tous les champs.");
                return;
            }


            // Check for duplicate name before adding a new product
            IngredientService ingServices = new IngredientService();
            if (ingServices.isIngredientNameExists(nomIng.getText())) {
                showAlert("Un ingredient avec ce nom existe déjà. ");
                return;
            }

            // Create a new Product object using les champs inclus
            Ingredient Ing = new Ingredient(nomIng.getText(), fileName, categorieIng.getValue());

            IngredientService ingServices1 = new IngredientService();
            ingServices1.addEntity(Ing);
            Path destinationPath = Paths.get("src/main/resources/images/IngredientsImages", fileName);
            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            showAlert("Ingredient ajouté avec succès.");

            // Effacer les champs après l'ajout réussi
            nomIng.clear();
            imageIng.clear();


        } catch (Exception e) {
            showAlert("Erreur lors de l'ajout de l'ingredient : " + e.getMessage());
        }
    }

    @FXML
    public void upload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload your profile picture");

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Check if the selected file has a valid extension (PNG or JPG)
            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                imageIng.setText(selectedFile.getPath());
            } else {
                // Handle the case where an invalid file is selected
                System.out.println("Invalid file format. Please select a PNG or JPG file.");
            }
        } else {
            // Handle the case where no file is selected
            System.out.println("No file selected");
        }

    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlertList(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void viderIngredient(ActionEvent event) {

        nomIng.clear();
        imageIng.clear();
    }


    @FXML
    void rafraichirIngredient(ActionEvent event) {

        IngredientService ingredientService = new IngredientService();
        ObservableList<Ingredient> list = FXCollections.observableList(ingredientService.getAllData());

        ingredientNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        IngredientCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
        IngredientImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        listing.setItems(list);

    }

    public void refreshList() {
        IngredientService ingService = new IngredientService();
        ObservableList<Ingredient> updatedList = FXCollections.observableList(ingService.getAllData());
        listing.setItems(updatedList);
    }

    @FXML
    void handleDelete() {
        Ingredient selectedIngredient = listing.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer cet ingrédient?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the selected item
                IngredientService ingService = new IngredientService();
                ingService.DeleteEntity(selectedIngredient);
                refreshList(); // Refresh the TableView
            }
        } else {
            // No item selected, show an information alert
            showAlertList(Alert.AlertType.INFORMATION, "Information", null, "Veuillez sélectionner un ingrédient à supprimer.");
        }
    }


    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        if (ingredient != null) {
            nomIng.setText(ingredient.getNom());
            categorieIng.setValue(ingredient.getCategorieing());
            imageIng.setText(ingredient.getImage());

            System.out.println(ingredient);
            System.out.println(imageIng.getText());
            URL imageUrl = getClass().getResource("/images/IngredientsImages/" + imageIng.getText());
            System.out.println(imageUrl);

        } else {
            System.out.println("Ingredient is null!");
        }
    }

    private Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>> createActionsCellFactory() {
        return new Callback<TableColumn<Ingredient, Void>, TableCell<Ingredient, Void>>() {
            @Override
            public TableCell<Ingredient, Void> call(final TableColumn<Ingredient, Void> param) {
                return new TableCell<Ingredient, Void>() {
                    private final Button btnDelete = new Button("Supprimer");
                    private final Button btnUpdate = new Button("Modifier");
                    private final Button btnDetails = new Button("Details");

                    {
                        btnDelete.setOnAction(event -> handleDelete());

                        btnUpdate.setOnAction(event -> {
                            Ingredient ING = getTableView().getItems().get(getIndex());
                            System.out.println(ING);
                            setIngredient(ING);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Ingredient currentING = getTableView().getItems().get(getIndex());
                            if (currentING != null) {
                                HBox buttonsBox = new HBox(btnDelete, btnUpdate,btnDetails);
                                setGraphic(buttonsBox);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }


        };
    }

    private boolean validateForm() {

        if (nomIng.getText() == null || categorieIng.getValue() == null || imageIng.getText() == null) {
            showAlert("Veuillez remplir tous les champs.");
            return false;
        }

        // Vérifier que tous les champs sont remplis
        if (nomIng.getText().isEmpty() || categorieIng.getValue().isEmpty() || imageIng.getText().isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
            return false;
        }


        // Check for duplicate name before adding a new product
        IngredientService ingServices = new IngredientService();
        if (ingServices.isIngredientNameExists(nomIng.getText())) {
            showAlert("Un ingredient avec ce nom existe déjà. ");
            return false;
        }
        return true;
    }



    @FXML
    void modifierIngredient(ActionEvent event) {
        if(validateForm()) {
            String picturePath = imageIng.getText();

            Path path = Paths.get(picturePath);
            String fileName = path.getFileName().toString();
            String selectedCategorie = categorieIng.getValue();

            ingredient.setNom(nomIng.getText());
            ingredient.setCategorieing(selectedCategorie);
            ingredient.setImage(fileName);

            IngredientService ingredientService = new IngredientService();
            System.out.println(ingredient);
            ingredientService.updateEntity(ingredient);


            Path destinationPath = Paths.get("src/main/resources/images/IngredientsImages", fileName);
            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert("Ingredient ajouté avec succès.");

            nomIng.clear();
            imageIng.clear();

        }
    }
}

