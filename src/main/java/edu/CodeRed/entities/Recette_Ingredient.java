package edu.CodeRed.entities;

public class Recette_Ingredient {

    private int id;
    private Recette recette;
    private Ingredient ingredient;

    public Recette_Ingredient() {
    }

    public Recette_Ingredient(Recette recette, Ingredient ingredient) {
        this.recette = recette;
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
