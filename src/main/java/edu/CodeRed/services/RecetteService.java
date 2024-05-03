package edu.CodeRed.services;

import edu.CodeRed.entities.Recette;
import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.interfaces.RService;
import edu.CodeRed.tools.MyConnexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetteService implements RService<Recette,Ingredient> {




    @Override
    public void addRecette(Recette recette, List<Ingredient> ingredients) {
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
    public void updateRecette(Recette recette, List<Ingredient> ingredients) {

        String updateRecipeQuery = "UPDATE recette SET nom = ?, categorie = ?, image = ?, description = ?, calorie_recette = ? WHERE id = ?";
        String deleteRecipeIngredientsQuery = "DELETE FROM recette_ingredient WHERE recette_id = ?";
        try {
            try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(updateRecipeQuery)) {
                ps.setString(1, recette.getNom());
                ps.setString(2, recette.getCategorie());
                ps.setString(3, recette.getImage());
                ps.setString(4, recette.getDescription());
                ps.setInt(5, recette.getCalorieRecette());
                ps.setInt(6, recette.getId());
                ps.executeUpdate();

                try (PreparedStatement psDelete = MyConnexion.getInstance().getCnx().prepareStatement(deleteRecipeIngredientsQuery)) {
                    psDelete.setInt(1, recette.getId());
                    psDelete.executeUpdate();
                }

                // Insert into recette_ingredients table to establish many-to-many relationship
                String insertRecipeIngredientsQuery = "INSERT INTO recette_ingredient (recette_id, ingredient_id) VALUES (?, ?)";
                try (PreparedStatement ps1 = MyConnexion.getInstance().getCnx().prepareStatement(insertRecipeIngredientsQuery)) {
                    for (Ingredient ingredient : ingredients) {
                        // Retrieve the ingredient ID from the database
                        int ingredientId = getIngredientId(ingredient);
                        // Insert the relationship
                        ps1.setInt(1, recette.getId());
                        ps1.setInt(2, ingredientId);
                        ps1.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Recipe updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve the recipe ID
    private int getRecetteId(Recette recette) {
        String query = "SELECT id FROM recette WHERE nom = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            ps.setString(1, recette.getNom());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Recette findById(int id) throws SQLException {
        Recette act = new Recette();

        try {
            String req = "SELECT * from recette where id='"+id+"'";
            Statement st = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                act.setId(rs.getInt("id"));
                act.setNom(rs.getString("nom"));
                act.setCategorie(rs.getString("categorie"));
                act.setImage(rs.getString("image"));
                act.setDescription(rs.getString("description"));
                act.setCalorieRecette(rs.getInt("calorie_recette"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return act;
    }






    public List<Recette> getAllDataRecette() {
        List<Recette> recettes = new ArrayList<>();
        String query = "SELECT * FROM recette";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Recette recette = new Recette();
                    int recetteId = rs.getInt("id");
                    recette.setId(recetteId);
                    recette.setNom(rs.getString("nom"));
                    recette.setCategorie(rs.getString("categorie"));
                    recette.setImage(rs.getString("image"));
                    recette.setDescription(rs.getString("description"));
                    recette.setCalorieRecette(rs.getInt("calorie_recette"));
                    recette.setIngredients(getIngredientsForRecette(recetteId)); // Retrieve ingredients for the current recipe
                    recettes.add(recette);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recettes;
    }

    // Method to retrieve ingredients for a specific recipe
    public List<Ingredient> getIngredientsForRecette(int recetteId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM ingredient i " +
                "JOIN recette_ingredient ri ON i.id = ri.ingredient_id " +
                "WHERE ri.recette_id = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            ps.setInt(1, recetteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setNom(rs.getString("nom"));
                    ingredient.setImage(rs.getString("image"));
                    ingredient.setCategorieing(rs.getString("categorieing"));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }


    @Override
    public void deleteRecette(int id) {

        String requete = "DELETE FROM recette WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);

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
    public Recette readRecette(String name) {
        Recette recette = null;
        String query = "SELECT * FROM recette WHERE nom = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    recette = new Recette();
                    recette.setId(rs.getInt("id"));
                    recette.setNom(rs.getString("nom"));
                    recette.setCategorie(rs.getString("categorie"));
                    recette.setImage(rs.getString("image"));
                    recette.setDescription(rs.getString("description"));
                    recette.setCalorieRecette(rs.getInt("calorie_recette"));
                    recette.setIngredients(getIngredientsForRecette(recette.getId())); // Retrieve ingredients for the current recipe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recette;
    }


}

