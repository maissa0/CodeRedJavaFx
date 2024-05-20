package edu.CodeRed.Controllers;



import edu.CodeRed.entities.Activite;
import edu.CodeRed.entities.Suivi_Activite;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.ActiviteService;
import edu.CodeRed.services.Suivi_ActiviteService;
import edu.CodeRed.tools.MyConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;




import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class AjouterSuiviControler implements Initializable {

    ActiviteService as = new ActiviteService();
    Suivi_Activite a = new Suivi_Activite();

    Suivi_ActiviteService sa= new Suivi_ActiviteService();

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker tDate;

    @FXML
    private ComboBox<String> comboActivity;

    @FXML
    private TextField tNbrRep;
    private  String nomActivity="";

    @FXML
    void ajoutersuivi(ActionEvent events) {


        a.setDate(Date.valueOf(tDate.getValue()));
        a.setRep(Integer.parseInt(tNbrRep.getText()));
        Preferences p = Preferences.userNodeForPackage(getClass());
        user user= new user();
        p.getInt("userId", user.getId());
        System.out.println(p.getInt("userId", user.getId()));

        Activite IdActivity = as.readActiviteByName(nomActivity);
        a.setActivite_id(IdActivity);
        //  UserId ajouté dynamiquement selon l'utilisateur connecté
        sa.addSuivi(a,a.getActivite_id().getId(),  p.getInt("userId", user.getId()));
        try {

            sendEmail1("mazizj2000@gmail.com","Ajout Suivi","Nouveau suivi ajouté pour l'activité "+a.getActivite_id().getNom() +" dans le "+a.getDate().toString()+" avec un nombre de répétitions "+String.valueOf(a.getRep()));
            Parent loader = FXMLLoader.load(getClass().getResource("/ListeSuivi.fxml"));
            comboActivity.getScene().setRoot(loader);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public void sendEmail1(String toAddress, String subject, String content) {
        // Configuration du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Gmail's SMTP server
        props.put("mail.smtp.port", "587"); // SMTP port for Gmail

// Enable authentication and TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Authentification
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // Remplacez par votre email et mot de passe
                return new PasswordAuthentication("mazizj2000@gmail.com", "sdma nypn zqic vfer");
            }
        });

        try {
            // Créez un nouveau message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mazizj2000@gmail.com")); // Expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress)); // Destinataire
            message.setSubject(subject);
            message.setText(content);

            // Envoyez le message
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + toAddress);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Activite> listActivity = FXCollections.observableList(as.afficherAllActivite());
        ObservableList<String> listNomActivity =  FXCollections.observableArrayList();
        for (Activite ac:listActivity) {
            listNomActivity.add(ac.getNom());
        }
        comboActivity.setItems(listNomActivity);
        comboActivity.setOnAction(event -> {
            // Récupérer l'option sélectionnée
            nomActivity=comboActivity.getValue();
        });
        LocalDate dateJour = LocalDate.now();

        tDate.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Désactiver les dates avant la date minimale
                        if (item.isBefore(dateJour)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Vous pouvez personnaliser le style si vous le souhaitez
                        }
                    }
                };
            }
        });




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
    private PanierController panierController;
    private VBox panierView;
    private Stage stage;
    private Scene panierScene;
    @FXML
    void openViewPanier(ActionEvent event)  throws IOException  {
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
    private ProduitController produitController;
    private VBox produitsViewFront;
    @FXML
    void openViewProduct(ActionEvent event) {
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        produitController.afficherProduitsFront(produitsViewFront);
        Scene scene = new Scene(produitsViewFront);
        produitsViewFront.setPrefSize(1095, 487);
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


    @FXML
    void listsuivi(ActionEvent event) throws IOException {

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeSuivi.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new FXML file
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // Close the current stage
        //currentStage.close();

        // Show the new stage
        stage.show();

    }

}






