package edu.CodeRed.services;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette_Ingredient;
import edu.CodeRed.interfaces.IService;
import edu.CodeRed.tools.MyConnexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.interfaces.IService;
import edu.CodeRed.tools.MyConnexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetteService implements IService<Recette,Ingredient> {


    @Override
    public void addEntity(Recette recette) {

    }

    @Override
    public void addEntitylist(Recette recette, List<Ingredient> ingredients) {
        String requette = "INSERT INTO recette (nom, categorie, image, description, calorie_recette) VALUES (?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(requette, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, recette.getNom());
                ps.setString(2, recette.getCategorie());
                ps.setString(3, recette.getImage());
                ps.setString(4, recette.getDescription());
                ps.setInt(5, recette.getCalorieRecette());
                ps.executeUpdate();

                // Get the auto-generated ID of the newly inserted recipe
                int recipeId;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        recipeId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve auto-generated keys.");
                    }
                }

                // Insert into recette_ingredients table to establish many-to-many relationship
                String insertRecipeIngredientsQuery = "INSERT INTO recette_ingredient (recette_id, ingredient_id) VALUES (?, ?)";
                try (PreparedStatement ps1 = MyConnexion.getInstance().getCnx().prepareStatement(insertRecipeIngredientsQuery)) {
                    System.out.println(recipeId);

                    for (Ingredient ingredient : ingredients) {
                        // Retrieve the ingredient ID from the database
                        int ingredientId = getIngredientId(ingredient);
                        System.out.println(ingredientId);
                        // Insert the relationship
                        ps1.setInt(1, recipeId);
                        ps1.setInt(2, ingredientId);
                        ps1.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Recipe added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve the ingredient ID from the database
    private int getIngredientId(Ingredient ingredient) throws SQLException {
        String selectIngredientQuery = "SELECT id FROM ingredient WHERE nom = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(selectIngredientQuery)) {
            ps.setString(1, ingredient.getNom());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Ingredient not found: " + ingredient.getNom());
                }
            }
        }
    }

    @Override
    public void updateEntity(Recette recette) {

    }

    @Override
    public List<Recette> getAllData() {
        return null;
    }

    @Override
    public void deleteEntity(int id) {

    }

    @Override
    public Recette readEntity(int id) {
        return null;
    }
}

