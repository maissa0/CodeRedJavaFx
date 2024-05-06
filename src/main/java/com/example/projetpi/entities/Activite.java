package com.example.projetpi.entities;

public class Activite {
    private int id;
    private String type;
    private String nom;
    private int nbr_cal;
    private String description;
    private String image;
    private String video;

    public Activite()
    {
    }

    public Activite(String type, String nom, int nbr_cal, String description,String image,String video) {
        this.type = type;
        this.nom = nom;
        this.nbr_cal = nbr_cal;
        this.description = description;
        this.image= image;
        this.video=video;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbr_cal() {
        return nbr_cal;
    }

    public void setNbr_cal(int nbr_cal) {
        this.nbr_cal = nbr_cal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Activite{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", nom='" + nom + '\'' +
                ", nbr_cal=" + nbr_cal +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                '}';
    }



}
