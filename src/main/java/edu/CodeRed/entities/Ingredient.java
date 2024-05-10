package edu.CodeRed.entities;

import java.util.List;

public class Ingredient {


    private int id ;
    private String nom;
    private String image;
    private String categorieing;
    private List<Ingredient> ingredients;

    public Ingredient() {
    }

    public Ingredient(String nom, String image, String categorieing) {
        this.nom = nom;
        this.image = image;
        this.categorieing = categorieing;
    }

    public Ingredient(String nom, String image) {
        this.nom = nom;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategorieing() {
        return categorieing;
    }

    public void setCategorieing(String categorieing) {
        this.categorieing = categorieing;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", categorieing='" + categorieing + '\'' +
                '}';
    }
}
