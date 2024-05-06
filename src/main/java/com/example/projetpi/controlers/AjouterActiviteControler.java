package com.example.projetpi.controlers;



import com.example.projetpi.entities.Activite;
import com.example.projetpi.services.ActiviteService;
import com.example.projetpi.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AjouterActiviteControler implements Initializable {

    ActiviteService as = new ActiviteService();
    MyConnection mc = new MyConnection();
    Activite a = new Activite();


    @FXML
    private ImageView ImageView;
    @FXML
    private Button btnAjouterAct;
    @FXML
    private Button btnModifier;
    @FXML
    private Button UploadBotton;

    @FXML
    private Text textArea;


    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Activite, String> colDescription;

    @FXML
    private TableColumn<Activite, Integer> colNbrCal;

    @FXML
    private TableColumn<Activite,String> colNom;

    @FXML
    private TableColumn<Activite,String> colType;

    @FXML
    private TableView<Activite> tableActivite;

    @FXML
    private TextField tDescription;


    @FXML
    private TextField tNbrcal;

    @FXML
    private TextField tNom;

    @FXML
    private TextField tType;

    @FXML
    private TextField tSearch;

    private Integer IdElement = 0;
    private String fileName ="";


    @FXML
    private void ajouterActivite(ActionEvent event)
    {
        a.setNom(tNom.getText());
        a.setType((tType.getText()));
        a.setNbr_cal(Integer.parseInt(tNbrcal.getText()));
        a.setDescription(tDescription.getText());
        a.setVideo("");
        a.setImage(fileName);
        as.addEntity(a);

        tNom.setText("");
        tType.setText("");
        tNbrcal.setText("");
        tDescription.setText("");
        ImageView.setImage(null);
        ObservableList<Activite> list = FXCollections.observableList(as.afficherAllActivite());
        tableActivite.setItems(list);


    }


    @FXML
    void UploadPhoto(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Get the file name
            fileName = selectedFile.getName();


            // Define the target file path in the project directory
            Path targetPath = Paths.get("src/main/resources/UploadedImages", fileName);

            try {
                // Copy the selected file to the project directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Load the image and set it to the ImageView
                Image image = new Image(targetPath.toUri().toString());
                ImageView.setImage(image);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void modifierActivite(ActionEvent event) {
        Activite a = as.readActivite(IdElement);
        a.setNom(tNom.getText());
        a.setType((tType.getText()));
        a.setNbr_cal(Integer.parseInt(tNbrcal.getText()));
        a.setDescription(tDescription.getText());
        if (fileName.equals("")){
            a.setImage(a.getImage());
        }else {
            a.setImage(fileName);
        }

        a.setVideo("");
        as.updateActivite(a);
        ObservableList<Activite> list = FXCollections.observableList(as.afficherAllActivite());
        tableActivite.setItems(list);



    }

    @FXML
    void searchActivite(ActionEvent events) {

        ObservableList<Activite> list = FXCollections.observableList(as.SearchActivite(tSearch.getText()));
        tableActivite.setItems(list);



    }

    @FXML
    void supprimerActivite(ActionEvent event) {

        if(IdElement != 0){
            as.supprimerActivite(as.readActivite(IdElement));
            ObservableList<Activite> list = FXCollections.observableList(as.afficherAllActivite());
            tableActivite.setItems(list);
            textArea.setText("");
        }
        else {
            textArea.setText("Veuillez selectionner une activité !");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Activite> list = FXCollections.observableList(as.afficherAllActivite());
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colNbrCal.setCellValueFactory(new PropertyValueFactory<>("nbr_cal"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableActivite.getColumns().addAll(colType, colNom, colNbrCal,colDescription);
        tableActivite.setItems(list);

        tableActivite.setOnMouseClicked((MouseEvent event) -> {
            // Check if a row is clicked
            if (event.getClickCount() == 1) {
                // Get the selected item (row)
                Activite selectedItem = tableActivite.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    Path targetPath = Paths.get("src/main/resources/UploadedImages", selectedItem.getImage());
                    Image image = new Image(targetPath.toUri().toString());
                    ImageView.setImage(image);
                    tNom.setText(selectedItem.getNom());
                    tType.setText(selectedItem.getType());
                    tNbrcal.setText(String.valueOf(selectedItem.getNbr_cal()));
                    tDescription.setText(selectedItem.getDescription());
                    IdElement = selectedItem.getId();

                }
            }
        });


    }

}
