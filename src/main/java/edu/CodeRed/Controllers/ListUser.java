package edu.CodeRed.Controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.services.userservice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class ListUser {



    @FXML
    private ComboBox<String> trilist;

    @FXML
    public TableColumn<user, Void> actions1;

    @FXML
    private TableView<user> list_user;

    @FXML
    public TextField searchbar_id;

    @FXML
    private TableColumn<user, String> user_Role;

    @FXML
    private TableColumn<user, String> user_adresse;

    @FXML
    private TableColumn<user, String> user_datedenaissance;

    @FXML
    private TableColumn<user, String> user_email;

    @FXML
    private TableColumn<user, String> user_genre;

    @FXML
    private TableColumn<user, Integer> user_id;

    @FXML
    private TableColumn<user, String> user_mdp;

    @FXML
    private TableColumn<user, String> user_nom;

    @FXML
    private TableColumn<user, String> user_numtel;

    @FXML
    private TableColumn<user, String > user_prenom;

    @FXML
    void genererPDF(ActionEvent event) {

    }


    private final userservice userService = new userservice(); // Initialize UserService

    //Redirect to Add user
    @FXML
    void      add_user(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addUser.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void initialize() {
        // Call the getAllUserData() method using the instance variable
        ObservableList<user> userList = FXCollections.observableList(userService.getalluserdata());

        // Set cell value factories for each TableColumn
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());
        // Set the items to the TableView
        list_user.setItems(userList);



        trilist.setOnAction(event -> triList(event));
        //search();


        ObservableList<String> Trichoices = FXCollections.observableArrayList("Nom", "Email");
        trilist.setItems(Trichoices);

        searchbar_id.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });



    }

    private Callback<TableColumn<user, Void>, TableCell<user, Void>> createActionsCellFactory() {
        return new Callback<TableColumn<user, Void>, TableCell<user, Void>>() {
            @Override
            public TableCell<user, Void> call(final TableColumn<user, Void> param) {
                return new TableCell<user, Void>() {
                    private final Button btnUpdate = new Button("Update");
                    private final Button btnDelete = new Button("Delete");

                    {
                        // Action for the update button
                        btnUpdate.setOnAction(event -> handleUpdate());
                        btnDelete.setOnAction(event -> handleDelete());
                    }

                    // This method is called whenever the cell needs to be updated
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Set the buttons in the cell
                            HBox buttons = new HBox(btnUpdate, btnDelete);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
    }
    @FXML
    void handleUpdate() {
        user selectedProjet = list_user.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            // Load the update scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent updateScene;
            try {
                updateScene = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Get the controller for the update scene
            UpdateUser updateController = loader.getController();
            updateController.setUser(selectedProjet);

            // Create a new stage for the update scene
            Stage updateStage = new Stage();
            updateStage.setTitle("Update User");
            updateStage.setScene(new Scene(updateScene));

            // Show the update stage
            updateStage.showAndWait();

            // After the update scene is closed, refresh the table view
            refreshList();
        } else {
            // No item selected, show an information alert
            showAlert(Alert.AlertType.INFORMATION, "Information", null, "Veuillez sélectionner un projet à mettre à jour.");
        }
    }

    @FXML
    void handleDelete() {
        user selectedProjet = list_user.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer ce projet?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the selected item
                userService.DeleteEntityWithConfirmation(selectedProjet);
                refreshList(); // Refresh the TableView
            }
        } else {
            // No item selected, show an information alert
            showAlert(Alert.AlertType.INFORMATION, "Information", null, "Veuillez sélectionner un projet à supprimer.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // Method to refresh the list of users
    @FXML
    public void refreshList() {
        ObservableList<user> updatedList = FXCollections.observableList(userService.getalluserdata());
        list_user.setItems(updatedList);
    }



    private void TrieNom() {
        userservice is = new userservice();
        List<user> i = is.triNom();
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());

        list_user.setItems(FXCollections.observableList(i));

    }

    private void Trieemail() {
        userservice is = new userservice();
        List<user> i = is.trimail();
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_mdp.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        user_datedenaissance.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        user_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_numtel.setCellValueFactory(new PropertyValueFactory<>("num_de_telephone"));
        actions1.setCellFactory(createActionsCellFactory());


        list_user.setItems(FXCollections.observableList(i));

    }


    private void filterTable(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            list_user.setItems(initialData());
        } else {
            ObservableList<user> filteredList = FXCollections.observableArrayList();
            for (user vehicule : list_user.getItems()) {
                if (vehicule.getNom().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(vehicule);
                }
            }
            list_user.setItems(filteredList);
        }
    }

    ObservableList<user>initialData(){
        userservice V = new userservice();
        return  FXCollections.observableArrayList(V.getalluserdata());
    }
    @FXML
    void triList(ActionEvent event) {

        if (trilist.getValue().equals("Nom")) {
            TrieNom();
        } else if (trilist.getValue().equals("Email")) {
            Trieemail();
        }
    }



    @FXML
    void genererPDF(MouseEvent event) {
        // Afficher la boîte de dialogue de sélection de fichier
        String downloadsPath = System.getProperty("user.home") + "/Downloads/";
        String fileName = "userlist.pdf";
        String filePath = downloadsPath + fileName;
        File selectedFile = new File(filePath);


        if (selectedFile != null) {
            // Générer le fichier PDF avec l'emplacement de sauvegarde sélectionné
            // Récupérer la liste des produits
            userservice as = new userservice();
            List<user> ingList = as.getalluserdata();

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

                PdfPCell cell4 = new PdfPCell(new Paragraph("Categorie", hrFont));
                cell4.setBackgroundColor(bgColor);
                cell4.setBorderColor(titleColor);
                cell4.setPaddingTop(20);
                cell4.setPaddingBottom(20);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell5 = new PdfPCell(new Paragraph("Categorie", hrFont));
                cell5.setBackgroundColor(bgColor);
                cell5.setBorderColor(titleColor);
                cell5.setPaddingTop(20);
                cell5.setPaddingBottom(20);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);




                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);

                Font hdFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
                BaseColor hdColor = new BaseColor(255, 255, 255); //
                hrFont.setColor(hrColor);

                // Ajouter les données des produits
                for (user act : ingList) {
                    PdfPCell cellR1 = new PdfPCell(new Paragraph(String.valueOf(act.getNom()), hdFont));
                    cellR1.setBorderColor(titleColor);
                    cellR1.setPaddingTop(10);
                    cellR1.setPaddingBottom(10);
                    cellR1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR1);

                    PdfPCell cellR2 = new PdfPCell(new Paragraph(act.getPrenom(), hdFont));
                    cellR2.setBorderColor(titleColor);
                    cellR2.setPaddingTop(10);
                    cellR2.setPaddingBottom(10);
                    cellR2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR2);

                    PdfPCell cellR3 = new PdfPCell(new Paragraph(String.valueOf(act.getGenre()), hdFont));
                    cellR3.setBorderColor(titleColor);
                    cellR3.setPaddingTop(10);
                    cellR3.setPaddingBottom(10);
                    cellR3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR3);

                    PdfPCell cellR4 = new PdfPCell(new Paragraph(String.valueOf(act.getNum_de_telephone()), hdFont));
                    cellR4.setBorderColor(titleColor);
                    cellR4.setPaddingTop(10);
                    cellR4.setPaddingBottom(10);
                    cellR4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR4);

                    PdfPCell cellR5 = new PdfPCell(new Paragraph(String.valueOf(act.getDate_de_naissance()), hdFont));
                    cellR5.setBorderColor(titleColor);
                    cellR5.setPaddingTop(10);
                    cellR5.setPaddingBottom(10);
                    cellR5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR5);

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

}
