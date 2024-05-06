package com.example.demo1.controller;

import com.example.demo1.database.DataBase;
import com.example.demo1.model.Commande;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class CommandeController {
    private Connection connection;
    private ImageView QRCodeImageView;

    public CommandeController() throws SQLException {
        this.connection = DataBase.getConnection();
        this.QRCodeImageView = new ImageView();

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
                        supprimerCommande(commande);
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
        TableColumn<Commande, Void> qrCodeColumn = new TableColumn<>("QRCode");
        qrCodeColumn.setCellFactory(param -> new TableCell<>() {
            private final Button qrCodeButton = new Button("QRCode");
            {
                qrCodeButton.setOnAction(event -> {
                    Commande selectedCommande = getTableRow().getItem(); // Get the selected Commande from the TableRow
                    if (selectedCommande != null) {
                        try {
                            // Call generateQRCode method to generate the QR code image
                            Image qrCodeImage = generateQRCode(selectedCommande);
                            displayQRCodeImage(qrCodeImage);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Show an error message if no commande is selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Veuillez sélectionner une commande.");
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(qrCodeButton);
                }
            }
        });


        // Ajouter les colonnes à la TableView
        tableView.getColumns().clear();
        tableView.getColumns().addAll(idColumn, dateCmdColumn, etatCmdColumn, editerColumn, supprimerColumn, detailsColumn,qrCodeColumn);

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
    public Image generateQRCode(Commande selectedCommande) throws InterruptedException {
        if (selectedCommande != null) {
            String eventData = selectedCommande.toString();
            System.out.println("Event data: " + eventData); // Adjust this based on your event data format
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.MARGIN, 0);
                BitMatrix bitMatrix = new QRCodeWriter().encode(eventData, BarcodeFormat.QR_CODE, 200, 150, hints);
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                System.out.println("QR code image generated");

            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] qrCodeBytes = outputStream.toByteArray();
            return new Image(new ByteArrayInputStream(qrCodeBytes));
        } else {
            return null;
        }
    }
    private void displayQRCodeImage(Image qrCodeImage) {
        Stage qrCodeStage = new Stage();
        qrCodeStage.setTitle("QR Code");
        ImageView imageView = new ImageView(qrCodeImage);
        imageView.setFitWidth(200); // Adjust width as needed
        imageView.setFitHeight(200); // Adjust height as needed
        qrCodeStage.setScene(new Scene(new StackPane(imageView), 220, 220)); // Adjust scene size as needed
        qrCodeStage.show();
    }
}


