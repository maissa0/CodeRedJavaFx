package edu.CodeRed.entities;
import java.util.List;
import edu.CodeRed.entities.Ingredient;

public class Recette {

    private int id;
    private String nom;
    private String categorie;
    private String image;
    private String description;
    private int calorieRecette;
    private List<Ingredient> ingredients;

    public Recette() {
    }

    public Recette(String nom, String categorie, String image, String description, int calorieRecette, List<Ingredient> ingredients) {
        this.nom = nom;
        this.categorie = categorie;
        this.image = image;
        this.description = description;
        this.calorieRecette = calorieRecette;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalorieRecette() {
        return calorieRecette;
    }

    public void setCalorieRecette(int calorieRecette) {
        this.calorieRecette = calorieRecette;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recette{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", calorieRecette=" + calorieRecette +
                ", ingredients=" + ingredients +
                '}';
    }
}