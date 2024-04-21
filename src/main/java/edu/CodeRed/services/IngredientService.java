package edu.CodeRed.services;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.interfaces.IService;
import edu.CodeRed.tools.MyConnexion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientService implements IService<Ingredient,Recette> {

    @Override
    public void addEntity(Ingredient ingredient) {
        String requette = "INSERT INTO ingredient (nom, image, categorieing) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, ingredient.getNom());
            pst.setString(2, ingredient.getImage());
            pst.setString(3, ingredient.getCategorieing());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                ingredient.setId(rs.getInt(1));
            }
            System.out.println("Ingredient added");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void addEntitylist(Ingredient ingredient, List<Recette> q) {

    }

    @Override
    public void updateEntity(Ingredient ingredient) {
        String requette = "UPDATE ingredient SET nom=?, image=?, categorieing=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);
            pst.setString(1, ingredient.getNom());
            pst.setString(2, ingredient.getImage());
            pst.setString(3, ingredient.getCategorieing());
            pst.setInt(4, ingredient.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" updated successfully.");
            } else {
                System.out.println("No ingredient found ");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public List<Ingredient> getAllData() {
        List<Ingredient> data = new ArrayList<>();
        String requette = "SELECT * FROM ingredient";

        try {
            Statement st = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requette);
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setNom(rs.getString("nom"));
                ingredient.setImage(rs.getString("image"));
                ingredient.setCategorieing(rs.getString("categorieing"));
                data.add(ingredient);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return data;
    }

    @Override
    public void deleteEntity(int id) {
        String requette = "DELETE FROM ingredient WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Ingredient with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No ingredient found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void DeleteEntityWithConfirmation(Ingredient ingredient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cet ingredient ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet ingredient ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression
            DeleteEntity(ingredient);
        } else {
            // L'utilisateur a annulé la suppression
            System.out.println("Suppression annulée");
        }
    }

    public void DeleteEntity(Ingredient ingredient) {
        String requete = "DELETE FROM ingredient WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, ingredient.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("ingredient Deleted");
            } else {
                System.out.println("ingredient not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Ingredient readEntity(int id) {
        String requette = "SELECT * FROM ingredient WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setNom(rs.getString("nom"));
                ingredient.setImage(rs.getString("image"));
                ingredient.setCategorieing(rs.getString("categorieing"));
                return ingredient;
            } else {
                System.out.println("No ingredient found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean isIngredientNameExists(String ingNom) {
        String requete = "SELECT * FROM ingredient WHERE nom = ?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, ingNom);
            ResultSet rs = pst.executeQuery();
            return rs.next();  // Returns true if a product with the given name already exists
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;  // Assume an error occurred, or the name doesn't exist
        }
    }
}
