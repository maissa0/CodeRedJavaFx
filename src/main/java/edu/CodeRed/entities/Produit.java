package edu.CodeRed.entities;

public class Produit {

    private String nom;
    private String description;
    private double prix;
    private String image;

    public Produit(String nom, String description, double prix,String image) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image=image;
    }

    public Produit() {

    }

    public String getNom() {
        return nom ;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image'" + image + '\'' +
                '}';
    }

}
