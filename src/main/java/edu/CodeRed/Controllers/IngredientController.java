package edu.CodeRed.Controllers;

import com.mysql.cj.xdevapi.JsonParser;
import edu.CodeRed.entities.Commande;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.IngredientService;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.prefs.Preferences;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class IngredientController {

    private Ingredient ingredient;
    @FXML
    private ImageView picture;

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
    private ComboBox<String> trierIng;

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

    ObservableList<Ingredient> dataList;

    @FXML
    private TextField nomIng;

    @FXML
    private Button viderIng;

    @FXML
    private ImageView imgIngredientInput;

    private ProduitController produitController;
    private CommandeController commandeController;
    TableView<Commande> tableView = new TableView<>();
    @FXML
    private VBox produitsView;
    @FXML
    private Stage stage;


    @FXML
    void initialize() {
        ObservableList<String> categoriesOptions = FXCollections.observableArrayList("cereales", "sucreries", "viandes", "legumes", "fruits", "produit laitiers");
        categorieIng.setItems(categoriesOptions);
        trierIng.setOnAction(event -> TriChoice(event));
        //search();


        ObservableList<String> Trichoices = FXCollections.observableArrayList("Nom", "Categorie");
        trierIng.setItems(Trichoices);

        IngredientService ingredientService = new IngredientService();
        ObservableList<Ingredient> list = FXCollections.observableList(ingredientService.getAllData());

        ingredientNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        IngredientCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
        IngredientImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        actions.setCellFactory(createActionsCellFactory());
        listing.setItems(list);

        searchbar_id.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });

        refreshList();

        produitsView = new VBox();
        try {
            produitController = new ProduitController();
            commandeController = new CommandeController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        stage =new Stage();

    }


    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            listing.setItems(initialData());
        } else {
            ObservableList<Ingredient> filteredList = FXCollections.observableArrayList();
            for (Ingredient ingr : listing.getItems()) {
                if (ingr.getNom().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(ingr);
                }
            }
            listing.setItems(filteredList);
        }
    }

    ObservableList<Ingredient>initialData(){
        IngredientService V = new IngredientService();
        return  FXCollections.observableArrayList(V.getAllData());
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
            Path destinationPath = Paths.get("C:","tools","pidev-code-red_test","public","img", fileName);


            try {
                Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            showAlert("Ingredient ajouté avec succès.");

            // Effacer les champs après l'ajout réussi
            nomIng.clear();
            imageIng.clear();
            imgIngredientInput.setImage(null);


        } catch (Exception e) {
            showAlert("Erreur lors de l'ajout de l'ingredient : " + e.getMessage());
        }
    }

    private File selectedImageFile;
    private String imageName = null;
    @FXML
    void upload(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(imgIngredientInput.getScene().getWindow());
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imgIngredientInput.setImage(image);

            // Generate a unique file name for the image
            String uniqueID = UUID.randomUUID().toString();
            String extension = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
            imageName = uniqueID + extension;

            // Set the text value of imageIng to the generated file name
            imageIng.setText(imageName);

            Path destination = Paths.get("C:","tools","pidev-code-red_test","public","img", imageName);
            Files.copy(selectedImageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
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


            Path destinationPath = Paths.get("C:","tools","pidev-code-red_test","public","img", fileName);
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


    @FXML
    void genererPDF(MouseEvent event) {
        // Afficher la boîte de dialogue de sélection de fichier
        String downloadsPath = System.getProperty("user.home") + "/Downloads/";
        String fileName = "ingredients.pdf";
        String filePath = downloadsPath + fileName;
        File selectedFile = new File(filePath);


        if (selectedFile != null) {
            // Générer le fichier PDF avec l'emplacement de sauvegarde sélectionné
            // Récupérer la liste des produits
            IngredientService as = new IngredientService();
            List<Ingredient> ingList = as.getAllData();

            try {
                // Créer le document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();



                // Créer une police personnalisée pour la date
                Font fontDate = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                BaseColor color = new BaseColor(114, 0, 0); // Rouge: 114, Vert: 0, Bleu: 0
                fontDate.setColor(color); // Définir la couleur de la police

                // Créer un paragraphe avec le lieu
                Paragraph tunis = new Paragraph("Ariana", fontDate);
                tunis.setIndentationLeft(455); // Définir la position horizontale
                tunis.setSpacingBefore(-30); // Définir la position verticale
                // Ajouter le paragraphe au document
                document.add(tunis);

                // Obtenir la date d'aujourd'hui
                LocalDate today = LocalDate.now();

                // Créer un paragraphe avec la date
                Paragraph date = new Paragraph(today.toString(), fontDate);

                date.setIndentationLeft(437); // Définir la position horizontale
                date.setSpacingBefore(1); // Définir la position verticale
                // Ajouter le paragraphe au document
                document.add(date);

                // Créer une police personnalisée
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD);
                BaseColor titleColor = new BaseColor(114, 0, 0); //
                font.setColor(titleColor);

                // Ajouter le contenu au document
                Paragraph title = new Paragraph("Liste des Ingredients", font);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50); // Ajouter une marge avant le titre pour l'éloigner de l'image
                title.setSpacingAfter(20);
                document.add(title);

                PdfPTable table = new PdfPTable(3); // 5 colonnes pour les 5 attributs des activités
                table.setWidthPercentage(100);
                table.setSpacingBefore(30f);
                table.setSpacingAfter(30f);

                // Ajouter les en-têtes de colonnes
                Font hrFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
                BaseColor hrColor = new BaseColor(255, 255, 255); //
                hrFont.setColor(hrColor);

                PdfPCell cell1 = new PdfPCell(new Paragraph("Nom", hrFont));
                BaseColor bgColor = new BaseColor(114, 0, 0);
                cell1.setBackgroundColor(bgColor);
                cell1.setBorderColor(titleColor);
                cell1.setPaddingTop(20);
                cell1.setPaddingBottom(20);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell2 = new PdfPCell(new Paragraph("Image", hrFont));
                cell2.setBackgroundColor(bgColor);
                cell2.setBorderColor(titleColor);
                cell2.setPaddingTop(20);
                cell2.setPaddingBottom(20);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell3 = new PdfPCell(new Paragraph("Categorie", hrFont));
                cell3.setBackgroundColor(bgColor);
                cell3.setBorderColor(titleColor);
                cell3.setPaddingTop(20);
                cell3.setPaddingBottom(20);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);


                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);

                Font hdFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
                BaseColor hdColor = new BaseColor(255, 255, 255); //
                hrFont.setColor(hdColor);
                // Ajouter les données des produits
                for (Ingredient act : ingList) {
                    PdfPCell cellR1 = new PdfPCell(new Paragraph(String.valueOf(act.getNom()), hdFont));
                    cellR1.setBorderColor(titleColor);
                    cellR1.setPaddingTop(10);
                    cellR1.setPaddingBottom(10);
                    cellR1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR1);

                    PdfPCell cellR2 = new PdfPCell(new Paragraph(act.getImage(), hdFont));
                    cellR2.setBorderColor(titleColor);
                    cellR2.setPaddingTop(10);
                    cellR2.setPaddingBottom(10);
                    cellR2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR2);

                    PdfPCell cellR3 = new PdfPCell(new Paragraph(String.valueOf(act.getCategorieing()), hdFont));
                    cellR3.setBorderColor(titleColor);
                    cellR3.setPaddingTop(10);
                    cellR3.setPaddingBottom(10);
                    cellR3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR3);

                }
                table.setSpacingBefore(20);
                document.add(table);
                document.close();


                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(selectedFile);
                } else {
                    System.out.println("Desktop is not supported on this platform.");
                }

                System.out.println("Le fichier PDF a été généré avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public IngredientService is = new IngredientService();
    /*void search() {
        FilteredList<Ingredient> filteredData = new FilteredList<>(FXCollections.observableArrayList(is.getAllData()), p -> true);
        searchbar_id.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(act -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String nom = String.valueOf(act.getNom());
                String Categorie = String.valueOf(act.getCategorieing());
                String lowerCaseFilter = newValue.toLowerCase();

                // Debug statements
                System.out.println("Search Query: " + lowerCaseFilter);
                System.out.println("Name: " + nom);
                System.out.println("Category: " + Categorie);

                if (nom.toLowerCase().contains(lowerCaseFilter)) {
                    System.out.println("Matched by Name: " + nom);
                    return true;
                } else if (Categorie.toLowerCase().contains(lowerCaseFilter)) {
                    System.out.println("Matched by Category: " + Categorie);
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Ingredient> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(listing.comparatorProperty());

        System.out.println("Filtered and Sorted Data: " + sortedData);

        //list apres sort
        listing.setItems(sortedData);

    }*/






    private void TrieNom() {
        IngredientService is = new IngredientService();
        List<Ingredient> i = is.triNomIngredient();
        ingredientNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        IngredientCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
        IngredientImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        actions.setCellFactory(createActionsCellFactory());


        listing.setItems(FXCollections.observableList(i));

    }

    private void TrieCategorie() {
        IngredientService is = new IngredientService();
        List<Ingredient> i = is.triCategorieIngredient();
        ingredientNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        IngredientCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieing"));
        IngredientImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        actions.setCellFactory(createActionsCellFactory());


        listing.setItems(FXCollections.observableList(i));

    }

    @FXML
    void TriChoice(ActionEvent event) {
        if (trierIng.getValue().equals("Nom")) {
            TrieNom();
        } else if (trierIng.getValue().equals("Categorie")) {
            TrieCategorie();
        }

    }

    /////////////////////////////////////////sidebar



    Preferences p = Preferences.userNodeForPackage(getClass());
    edu.CodeRed.entities.user user= new user();
    @FXML
    void back_to_login(ActionEvent event) throws IOException {

        p.getInt("userId", 0);
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/login.fxml"));
        //searchbar.getChildren().setAll(pane);
        System.out.println(p.getInt("userId hedhi logout", user.getId()));

    }



    @FXML
    void openViewActitvite(ActionEvent event) throws IOException {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterActivite.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewCommande(ActionEvent event) {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        commandeController.afficherCommandes(tableView);
        Scene scene = new Scene(tableView);
        tableView.setPrefSize(800, 600);
        stage.setScene(scene);


        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewIngredient(ActionEvent event) throws IOException {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ingredient.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewObjectif(ActionEvent event) throws IOException {

        /*Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ADDCRUD.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();*/
    }

    @FXML
    void openViewProduit(ActionEvent event) {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file


        // Load the new FXML file
        commandeController.afficherCommandes(tableView);
        Scene scene = new Scene(tableView);
        tableView.setPrefSize(800, 600);
        stage.setScene(scene);


        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();


        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewRecette(ActionEvent event) throws IOException {

        // Get the MenuItem that was clicked
        MenuItem menuItem = (MenuItem) event.getSource();

        // Get the ContextMenu of the MenuItem
        ContextMenu menu = menuItem.getParentPopup();

        // Get the Stage of the ContextMenu
        Stage currentStage = (Stage) menu.getOwnerWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewRecette.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewSuiviObj(ActionEvent event) throws IOException {
        /*Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ADDCRUD.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();*/
    }

    @FXML
    void openViewUser(ActionEvent event) {

    }



}


