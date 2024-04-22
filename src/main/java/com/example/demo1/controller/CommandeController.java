package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandeController {
    private Connection connection;

    public CommandeController() throws SQLException {
        this.connection = DataBase.getConnection();

    }

    public void afficherCommandes(TableView<Commande> tableView) {
        // Récupérer la liste des commandes depuis la base de données
        List<Commande> commandes = getAllCommandes();

        // Créer les colonnes de la TableView
        TableColumn<Commande, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Commande, LocalDate> dateCmdColumn = new TableColumn<>("Date");
        dateCmdColumn.setCellValueFactory(new PropertyValueFactory<>("dateCmd"));

        TableColumn<Commande, String> etatCmdColumn = new TableColumn<>("Etat");
        etatCmdColumn.setCellValueFactory(new PropertyValueFactory<>("etatCmd"));


        TableColumn<Commande, Void> editerColumn = new TableColumn<>("Actions");
        // À l'intérieur de la méthode afficherCommandes dans votre CommandeController
        // À l'intérieur de la méthode afficherCommandes dans votre CommandeController
        editerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editerButton = new Button("Editer");
            {
                editerButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());

                    // Créer des champs de texte pré-remplis avec les valeurs de la commande sélectionnée
                    TextField editedDateCmdField = new TextField(commande.getDateCmd().toString());
                    ComboBox<String> editedEtatCmdField = new ComboBox<>();
                    editedEtatCmdField.getItems().addAll("En cours", "Validé");
                    editedEtatCmdField.setValue(commande.getEtatCmd());
                    TextField editedQuantiteCmdField = new TextField(String.valueOf(commande.getQuantiteCmd()));
                    TextField editedTotalField = new TextField(String.valueOf(commande.getTotal()));

                    // Créer le bouton "Enregistrer" et définir son événement d'action
                    Button saveButton = new Button("Enregistrer");
                    saveButton.setOnAction(saveEvent -> {
                        // Récupérer les nouvelles valeurs des champs de texte
                        LocalDate newDateCmd = LocalDate.parse(editedDateCmdField.getText());
                        String newEtatCmd = editedEtatCmdField.getValue();
                        int newQuantiteCmd = Integer.parseInt(editedQuantiteCmdField.getText());
                        double newTotal = Double.parseDouble(editedTotalField.getText());

                        // Vérifier si la quantité est différente de zéro
                        if (newQuantiteCmd != 0) {
                            // Appeler la méthode pour éditer la commande dans le contrôleur
                            editerCommande(commande.getId(), newDateCmd, newEtatCmd, newQuantiteCmd, newTotal);

                            // Rafraîchir l'affichage des commandes
                            afficherCommandes(tableView);
                        } else {
                            // Afficher un message d'erreur si la quantité est égale à zéro
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Erreur");
                            alert.setContentText("La quantité de commande ne peut pas être égale à zéro.");
                            alert.showAndWait();
                        }
                    });

                    // Remplacer les informations existantes par les champs de texte et le bouton "Enregistrer"
                    setGraphic(new VBox(
                            new Label("Date de la commande:"), editedDateCmdField,
                            new Label("État de la commande:"), editedEtatCmdField,
                            new Label("Quantité de la commande:"), editedQuantiteCmdField,
                            new Label("Total de la commande:"), editedTotalField,
                            saveButton
                    ));
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editerButton);
                }
            }

        });
        TableColumn<Commande, Void> supprimerColumn = new TableColumn<>("Actions");
        supprimerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");

            {
                supprimerButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());

                    // Créer une boîte de dialogue de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Suppression de commande");
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer cette commande?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Supprimer la commande uniquement si l'utilisateur confirme
                        // Ajoutez ici la logique pour supprimer la commande sélectionnée
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(supprimerButton);
                }
            }
        });
        TableColumn<Commande, Void> detailsColumn = new TableColumn<>("Détails");
        detailsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button detailsButton = new Button("Détails");

            {
                detailsButton.setOnAction(event -> {
                    Commande commande = getTableView().getItems().get(getIndex());

                    // Créer et afficher une boîte de dialogue de détails de commande
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Détails de la commande");
                    alert.setHeaderText(null);
                    alert.setContentText("ID: " + commande.getId() + "\n" +
                            "Quantité : " + commande.getQuantiteCmd() + "\n" +
                            "Total: " + commande.getTotal());
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        });


        // Ajouter les colonnes à la TableView
        tableView.getColumns().clear();
        tableView.getColumns().addAll(idColumn, dateCmdColumn, etatCmdColumn,editerColumn, supprimerColumn,detailsColumn);

        // Ajouter les commandes à la TableView
        ObservableList<Commande> commandeObservableList = FXCollections.observableArrayList(commandes);
        tableView.setItems(commandeObservableList);
    }
    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();

        try (Connection connection = DataBase.getConnection()) {
            String query = "SELECT id, date_cmd, etat_cmd, qte_cmd, total FROM commande";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                LocalDate dateCmd = resultSet.getDate("date_cmd").toLocalDate();
                String etatCmd = resultSet.getString("etat_cmd");
                int quantiteCmd = resultSet.getInt("qte_cmd");
                double total = resultSet.getDouble("total");

                Commande commande = new Commande(id, dateCmd, etatCmd, quantiteCmd, total);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }
    public void editerCommande(int id, LocalDate newDateCmd, String newEtatCmd, int newQuantiteCmd, double newTotal) {
        try {
            String query = "UPDATE commande SET date_cmd = ?, etat_cmd = ?, qte_cmd = ?, total = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(newDateCmd));
            statement.setString(2, newEtatCmd);
            statement.setInt(3, newQuantiteCmd);
            statement.setDouble(4, newTotal);
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void supprimerCommande(Commande commande) {
        try {
            String query = "DELETE FROM commande WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, commande.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
