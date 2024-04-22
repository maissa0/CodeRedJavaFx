package edu.CodeRed.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import edu.CodeRed.entities.user;
import java.io.IOException;
import javafx.collections.ObservableList;

public class ListUser {

    @FXML
    private TableColumn<?, ?> actions;

    @FXML
    private TableView<?> list_user;

    @FXML
    private TextField searchbar_id;

    @FXML
    private TableColumn<?, ?> user_adresse;

    @FXML
    private TableColumn<?, ?> user_daten;

    @FXML
    private TableColumn<?, ?> user_email;

    @FXML
    private TableColumn<?, ?> user_genre;

    @FXML
    private TableColumn<?, ?> user_id;

    @FXML
    private TableColumn<?, ?> user_level;

    @FXML
    private TableColumn<?, ?> user_nom;

    @FXML
    private TableColumn<?, ?> user_prenom;

    @FXML
    private TableColumn<?, ?> user_pwd;

    @FXML
    private TableColumn<?, ?> user_tel;










    //Redirect to Add user
    @FXML
    void add_user(ActionEvent event) throws IOException {
        Home.loadFXML("/addUser.fxml");
    }
    //Table View initialization
    @FXML
    void initialize() {
        //ObservableList<user> list = FXCollections.observableList(edu.CodeRed.services.userservice.getalluserdata());
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        user_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_tel.setCellValueFactory(new PropertyValueFactory<>("numéro de téléphone"));
        user_nom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        user_prenom.setCellValueFactory(new PropertyValueFactory<>("Prénom"));
        user_daten.setCellValueFactory(new PropertyValueFactory<>("Date de naissance"));
        user_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
    }}
       //actions.setCellFactory(createActionsCellFactory());
      // list_user.setItems(list);}

