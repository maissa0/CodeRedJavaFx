package edu.CodeRed.tests;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.services.RecetteService;
import edu.CodeRed.tools.MyConnexion;

import java.util.ArrayList;
import java.util.List;


public class MainClass {


    public static void main(String[] args) {
        //MyConnexion mc = new MyConnexion();

        // Create a new instance of IngredientService
        //IngredientService ingredientService = new IngredientService();

        // Test adding a new ingredient
        //Ingredient ingredientToAdd = new Ingredient("ons", "ons.jpg", "Grain");
        //ingredientService.addEntity(ingredientToAdd);

        // Test getting all ingredients
       // List<Ingredient> allIngredients = ingredientService.getAllData();
       // System.out.println("All Ingredients:");
       // for (Ingredient ingredient : allIngredients) {
       //     System.out.println(ingredient);
       // }

        // Test updating an ingredient
        //Ingredient ingredientToUpdate = allIngredients.get(0);
        //ingredientToUpdate.setNom("Updated Sugar");
        //ingredientService.updateEntity(ingredientToUpdate, ingredientToUpdate.getId());

        // Test getting a specific ingredient
       // int ingredientIdToGet = allIngredients.get(0).getId();
        //Ingredient retrievedIngredient = ingredientService.readEntity(ingredientIdToGet);
       // System.out.println("Retrieved Ingredient:");
       // System.out.println(retrievedIngredient);

        // Test deleting an ingredient
       // int ingredientIdToDelete = allIngredients.get(0).getId();
        //ingredientService.deleteEntity(ingredientIdToDelete);


        // Create a new instance of RecetteService
        RecetteService recetteService = new RecetteService();

        // Create a sample recipe
        Recette recette = new Recette();
        recette.setNom("Test Recipe123");
        recette.setCategorie("Test Category");
        recette.setImage("test.jpg");
        recette.setDescription("This is a test recipe description");
        recette.setCalorieRecette(500); // Example calorie count

        // Create a list of sample ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setNom("Flour");
        ingredient1.setImage("flour.jpg");
        ingredient1.setCategorieing("Grain");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setNom("Sugar");
        ingredient2.setImage("sugar.jpg");
        ingredient2.setCategorieing("Sweetener");
        ingredients.add(ingredient2);

        // Test the addEntity method
        recetteService.addEntitylist(recette, ingredients);
    }

}

