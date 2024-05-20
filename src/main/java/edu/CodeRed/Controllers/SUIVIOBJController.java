package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Objectif;
import edu.CodeRed.entities.SuivieObjectif;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.ServiceObjectif;
import edu.CodeRed.services.ServiceSuiviObj;
import edu.CodeRed.tools.MyConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;

public class SUIVIOBJController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button panierButton;
    @FXML
    private VBox produitsView;
    private VBox produitsViewFront;

    private Stage stage;
    private ProduitController produitController;

    @FXML
    private DatePicker date;

    @FXML
    private TextField nouvPoid;

    @FXML
    private ComboBox<Integer> weightComboBox;
    private List<Objectif> objectifs;

    @FXML
    private VBox panierView;
    private PanierController panierController;
    private Scene panierScene;


    @FXML
    private TextField tAge;

    @FXML
    private TextField tCalorie;

    @FXML
    private TextField tHeight;

    @FXML
    private TextField tID;

    @FXML
    private Button calculateBtn;
    @FXML
    private Button calculateIMCBtn;
    Preferences p = Preferences.userNodeForPackage(getClass());
    edu.CodeRed.entities.user user= new user();



    @FXML
    private TextField tWeight;

    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);


    @FXML
    void afficherSuivi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CRUDSUIVI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ouverture de la fenêtre");
            alert.setContentText("Une erreur s'est produite lors de l'ouverture de la fenêtre. Veuillez réessayer.");
            alert.showAndWait();
        }
    }


    @FXML
    void createSuivieObjectif(ActionEvent event) {


        ServiceSuiviObj serviceSuiviObj = new ServiceSuiviObj();

        if (date.getValue() == null || nouvPoid.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Tu ne peux pas faire ça. Assure-toi de remplir la date et le nouveau poids.");
            alert.showAndWait();
            return;
        }
        //  int objId = Integer.parseInt(objectif_id.getText());
        //int idValue = Integer.parseInt(id.getText());
        // int nouvPoidValue = Integer.parseInt(nouvPoid.getText());
        // Date dateValue = Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create a new SuivieObjectif object

        SuivieObjectif suivieObjectif = new SuivieObjectif();
        suivieObjectif.setDate(java.sql.Date.valueOf(date.getValue()));
        suivieObjectif.setNouvPoid(Integer.parseInt(nouvPoid.getText()));
        int selectWiegth = weightComboBox.getValue();
        long selecteId = ServiceObjectif.getIdWeigthByWiegth(selectWiegth);
        if (selecteId == -1) {
            System.out.println("ID de poids non trouvé pour le poids sélectionné: " + selectWiegth);
            return; // Arrêtez l'exécution si l'ID de poids n'est pas trouvé
        }
        Objectif o = new Objectif();
        o.setId((int) selecteId);
        suivieObjectif.setObjectif(o);

        try {
            serviceSuiviObj.add(suivieObjectif);
            if (suivieObjectif.getNouvPoid() > selectWiegth) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Prise de poids, maintenant pour reste sur le meme poids n augmente pas vos calorie !");
                alert.show();
            } else if (suivieObjectif.getNouvPoid() < selectWiegth) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Perte de poids, bien tu peux dimunier les nombre de calorie pours perte du poids!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setContentText("Ton poids est inchangé!");
                alert.show();
            }



            initialize();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText(e.getMessage());
            alert.show();
            throw new RuntimeException(e);
        }
    }


    private void loadObjectifs() {
        objectifs = new ArrayList<>();
        Statement stmt = null;
        try {
            Connection conn = MyConnexion.getInstance().getCnx();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Objectif");
            while (rs.next()) {
                int id = rs.getInt("id");
                String sexe = rs.getString("sexe");
                int age = rs.getInt("age");
                int height = rs.getInt("height");
                int weight = rs.getInt("weight");
                String activity_level = rs.getString("activity_level");
                String objectif = rs.getString("objectif");
                int calorie = rs.getInt("calorie");
                objectifs.add(new Objectif(id, sexe, age, height, weight, activity_level, objectif, calorie));
                System.out.println("Objectif added: " + objectif); // Debug print statement
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    public void initialize() throws SQLException {
        ServiceObjectif o = new ServiceObjectif();
        o.chargerweight(weightComboBox);

        // Ajouter une validation pour que l'attribut 'nouvPoid' accepte uniquement des chiffres
        nouvPoid.setTextFormatter(new TextFormatter<>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if (change.isContentChange()) {
                    if (!change.getControlNewText().matches("\\d*")) {
                        return null;
                    }
                }
                return change;
            }
        }));
        produitsViewFront=new VBox();
        produitController = new ProduitController();
        stage=new Stage();
        panierView = new VBox();
        panierController=new PanierController();
        panierScene = new Scene(panierView);
    }

    @FXML
    void suiviobjfront(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SUIVIOBJ.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ouverture de la fenêtre");
            alert.setContentText("Une erreur s'est produite lors de l'ouverture de la fenêtre. Veuillez réessayer.");
            alert.showAndWait();
        }
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
        //currentStage.close();

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


    // On garde une référence à la scène du panier
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




}

