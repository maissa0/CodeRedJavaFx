package edu.CodeRed.Controllers;

import edu.CodeRed.entities.Commande;
import edu.CodeRed.entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class Admin {

    @FXML
    private Label Nom;

    @FXML
    private TableColumn<?, ?> actions1;

    @FXML
    private MenuItem gesIng;

    @FXML
    private MenuItem gesIng1;

    @FXML
    private MenuItem gesIng11;

    @FXML
    private MenuItem gesIng111;

    @FXML
    private MenuItem getRec;

    @FXML
    private MenuItem getRec1;

    @FXML
    private MenuItem getRec11;

    @FXML
    private MenuItem getRec111;

    @FXML
    private TableView<?> list_user;

    @FXML
    private ImageView picture;

    @FXML
    private AnchorPane recpane;

    @FXML
    private Label role;

    @FXML
    private AnchorPane searchbar;

    @FXML
    private AnchorPane searchbar1;

    @FXML
    private TextField searchbar_id;

    @FXML
    private ComboBox<?> trilist;

    @FXML
    private TableColumn<?, ?> user_Role;

    @FXML
    private TableColumn<?, ?> user_adresse;

    @FXML
    private TableColumn<?, ?> user_datedenaissance;

    @FXML
    private TableColumn<?, ?> user_email;

    @FXML
    private TableColumn<?, ?> user_genre;

    @FXML
    private TableColumn<?, ?> user_id;

    @FXML
    private TableColumn<?, ?> user_mdp;

    @FXML
    private TableColumn<?, ?> user_nom;

    @FXML
    private TableColumn<?, ?> user_numtel;

    @FXML
    private TableColumn<?, ?> user_prenom;
    private ProduitController produitController;
    private CommandeController commandeController;
    TableView<Commande> tableView = new TableView<>();
    @FXML
    private VBox produitsView;
    @FXML
    private Stage stage;
    public void initialize() throws SQLException {
        produitsView = new VBox();
        produitController = new ProduitController();
        commandeController = new CommandeController();
        stage =new Stage();

    }
    @FXML
    void Ingr(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/ingredient.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void Objectif(ActionEvent event)  throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/CRUD.fxml"));
        recpane.getChildren().setAll(pane);
    }
    @FXML
    void Recette(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/viewRecette.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void SuivieObjectif(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/CRUDSUIVI.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void add_user(ActionEvent event) {

    }
    Preferences p = Preferences.userNodeForPackage(getClass());
    edu.CodeRed.entities.user user= new user();
    @FXML
    void back_to_login(ActionEvent event)  throws IOException {
        p.getInt("userId", 0);
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/login.fxml"));
        searchbar.getChildren().setAll(pane);
        System.out.println(p.getInt("userId hedhi logout", user.getId()));
    }

    @FXML
    void openViewActitvite(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/AjouterActivite.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewCommande(ActionEvent event) {
        commandeController.afficherCommandes(tableView);
        Scene scene = new Scene(tableView);
        tableView.setPrefSize(800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void openViewIngredient(ActionEvent event)throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/ingredient.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewObjectif(ActionEvent event)throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/CRUD.fxml"));
        recpane.getChildren().setAll(pane);
    }
    @FXML
    void openViewProduit(ActionEvent event) {
            produitController.afficherProduits(produitsView);
            Scene scene = new Scene(produitsView);
            produitsView.setPrefSize(800, 600);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    void openViewRecette(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/viewRecette.fxml"));
        recpane.getChildren().setAll(pane);
    }
    @FXML
    void openViewSuiviObj(ActionEvent event)throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/CRUDSUIVI.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void openViewUser(ActionEvent event) throws IOException {

        AnchorPane pane = FXMLLoader.load(getClass().getResource("/listUser.fxml"));
        recpane.getChildren().setAll(pane);
    }

    @FXML
    void triList(ActionEvent event) {

    }

}
