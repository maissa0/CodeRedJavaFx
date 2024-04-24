package edu.CodeRed.entities;

public class user {
    private int id;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String date_de_naissance;
    private String role;
    private String genre;
    private String adresse;
    private String num_de_telephone;





    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDate_de_naissance() {
        return date_de_naissance;
    }

    public void setDate_de_naissance(String date_de_naissance) {
        this.date_de_naissance = date_de_naissance;
    }

    public String getrole() {
        return role;
    }

    public void setrole(String role) {
        this.role = role;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNum_de_telephone() {
        return num_de_telephone;
    }

    public void setNum_de_telephone(String num_de_telephone) {
        this.num_de_telephone = num_de_telephone;
    }

    //constructors for add
public user( String email,String nom, String prenom, String date_de_naissance, String role,String genre , String num_de_telephone, String adresse, String password) {

    this.email = email;

    this.password = password;
    this.nom = nom;
    this.prenom = prenom;
    this.date_de_naissance = date_de_naissance;
    this.role = role;
    this.genre = genre;
    this.adresse = adresse;
    this.num_de_telephone = num_de_telephone;

}
    //constructors

    public user(int id, String email, String password, String nom, String prenom, String date_de_naissance, String role, String genre, String adresse, String num_de_telephone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.date_de_naissance = date_de_naissance;
        this.role = role;
        this.genre = genre;
        this.adresse = adresse;
        this.num_de_telephone = num_de_telephone;
    }

    //Login Constructor
  /*  public user(int id, String email,  String password, String nom, String prenom, String date_de_naissance, String genre, String adresse, String num_de_telephone, String role) {
        this.id = id;
        this.email = email;

        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.date_de_naissance = date_de_naissance;
        this.genre = genre;
        this.adresse = adresse;
        this.num_de_telephone = num_de_telephone;
        role = role;
    }*/
    //update constructor

    //to String


    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", email='" + email + '\'' +

                ", password='" + password + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", date_de_naissance='" + date_de_naissance + '\'' +
                ", role='" + role + '\'' +
                ", genre='" + genre + '\'' +

                ", adresse='" + adresse + '\'' +
                ", num_de_telephone='" + num_de_telephone + '\'' +

                '}';
    }

}

