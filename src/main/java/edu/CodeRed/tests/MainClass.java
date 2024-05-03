package edu.CodeRed.tests;

import edu.CodeRed.entities.Ingredient;
import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.services.IngredientService;
import edu.CodeRed.services.JournalService;
import edu.CodeRed.services.RecetteService;
import edu.CodeRed.tools.MyConnexion;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainClass {


    public static void main(String[] args) throws SQLException {
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
        //RecetteService recetteService = new RecetteService();

        // Create a sample recipe
        /*Recette recette = new Recette();
        recette.setNom("Test Recipe");
        recette.setCategorie("Test Category");
        recette.setImage("maissa.jpg");
        recette.setDescription("This is a recipe description");
        recette.setCalorieRecette(555); // Example calorie count

        // Create a list of sample ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setNom("tarrot");
        ingredient1.setImage("carrot.jpg");
        ingredient1.setCategorieing("Vegetable");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setNom("Sugar");
        ingredient2.setImage("sugar.jpg");
        ingredient2.setCategorieing("Sweetener");
        ingredients.add(ingredient2);*/

        // Test the addEntity method
        //System.out.println(recetteService.readRecette("Test Recipe"));


        RecetteService recetteService = new RecetteService();
        JournalService journalService = new JournalService();

// Fetch a list of recettes
        List<Recette> recettes = recetteService.getAllDataRecette();

// Creating a Journal
        Journal journal = new Journal();
        journal.setUserId(1);
// Change the date
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JUNE, 23); // Set the desired date
        Date date = cal.getTime();
        journal.setDate(date);

// Adding recettes to the journal
        List<Recette> recettesToJournal = new ArrayList<>();
        recettesToJournal.add(recettes.get(0)); // Adding the first recette from the list
        recettesToJournal.add(recettes.get(1)); // Adding the second recette from the list

// Adding the Journal


        //journalService.addJournal(journal,recettesToJournal);




        // Call the deleteJournal method
        System.out.println(journalService.getTop5MostUsedRecettes());




    }



}

