package edu.CodeRed.Controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class addJournalController implements Initializable {

    Preferences p = Preferences.userNodeForPackage(getClass());
    user user= new user();
    @FXML
    private ScrollPane Rec_ScrollPane;

    @FXML
    private TableColumn<Recette, String> categRecCol;

    @FXML
    private GridPane ingredient_gridPane;

    @FXML
    private TableView<Recette> listRec;

    @FXML
    private TableColumn<Recette, String> nomRecCol;

    @FXML
    private DatePicker txtDateJournal;

    RecetteService rs = new RecetteService();
    List<Recette> recetteList = rs.getAllDataRecette();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 0;
        int row = 1;
        for (int i = 0; i < recetteList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/CardDesignRecette.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CardDesignRecetteController itemController = fxmlLoader.getController();
            itemController.setRecetteData(recetteList.get(i));


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

        produitsViewFront=new VBox();
        try {
            produitController = new ProduitController();
            panierController=new PanierController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stage=new Stage();
        panierView = new VBox();

        panierScene = new Scene(panierView);
    }

    @FXML
    void addJournal(ActionEvent event) {
        if(txtDateJournal.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez la date de votre journal.");
            Optional<ButtonType> option = alert.showAndWait();
        } else {
            ajouterJournal();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ajouté avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Journal a été ajoutée avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    public ObservableList<Recette> CardListDataRecette = FXCollections.observableArrayList();
    void ajouterJournal(){
        List<Recette> recList;
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/CardDesignRecette.fxml"));
            AnchorPane pane = load.load();
            CardDesignRecetteController item = load.getController();
            recList = item.getListRecToJournal();
            if(recList.isEmpty()){
                System.out.println("erreur");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!recList.isEmpty()){
            for(int i=0;i<recList.size();i++){
                CardListDataRecette.add(recList.get(i));
            }
            nomRecCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            categRecCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            listRec.setItems(CardListDataRecette);
        }

        Date dateJournal=null;
        try {
            LocalDate localDate = txtDateJournal.getValue();
            if (localDate != null) {
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                dateJournal = Date.from(instant);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        JournalService js = new JournalService();
        Journal j = new Journal( p.getInt("userId", user.getId()),dateJournal);
        js.addJournal(j,recList);
        send_SMS();
    }


    @FXML
    void objectiffront(ActionEvent event) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ADDCRUD.fxml"));
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
    void openViewCommand(ActionEvent event) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
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
    void openViewJournal(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewJournal.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }


    private VBox produitsViewFront;

    private Stage stage;
    private ProduitController produitController;
    @FXML
    private VBox panierView;
    private PanierController panierController;
    private Scene panierScene; // On garde une référence à la scène du panier
    @FXML
    void openViewPanier(ActionEvent event) throws IOException {


        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        panierController.afficherProduitsDuPanier();
        Scene scene = new Scene(panierView);

        panierView.setPrefSize(800, 600);

        stage.setScene(panierScene);



        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewProduct(ActionEvent event) throws IOException {


        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        produitController.afficherProduitsFront(produitsViewFront);
        Scene scene = new Scene(produitsViewFront);
        produitsViewFront.setPrefSize(800, 600);
        stage.setScene(scene);


        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    @FXML
    void openViewRecettes(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListFrontRecette.fxml"));
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
    void openViewSuivAct(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSuivi.fxml"));
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
    void openViewSuivObj(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SUIVIOBJ.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        currentStage.close();

        // Show the new stage
        stage.show();
    }

    JournalService j = new JournalService();
    void send_SMS (){
        // Initialisation de la bibliothèque Twilio avec les informations de votre compte
        String ACCOUNT_SID = "AC52b4c1f2b1f2ecfe3a48c85b038d88d0";
        String AUTH_TOKEN = "0774eed704b3f40dff0e9883f11ae248";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recipientNumber = "+21655845445";
        String message = "Bonjour Mr ,\n"
                + "Nous sommes ravis de vous informer qu'un planning a été ajouté.\n "
                + "Veuillez contactez l'administration pour plus de details.\n "
                + "Merci de votre fidélité et à bientôt chez optihealth.\n"
                + "Votre total calorie pour aujourd'hui est de "+ j.getCaloriesForToday(p.getInt("userId", user.getId()))
                + "Cordialement,\n"
                ;

        Message twilioMessage = Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber("+14698080489"),message).create();

        System.out.println("SMS envoyé : " + twilioMessage.getSid());
        //  TrayNotificationAlert.notif("Coupon", "Coupon sent successfully.",
        //NotificationType.SUCCESS, AnimationType.POPUP, Duration.millis(2500));



    }
}
