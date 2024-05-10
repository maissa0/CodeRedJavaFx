package edu.CodeRed.Controllers;

public class UserSession {
    private static UserSession instance;

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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDate_de_naissance() {
        return date_de_naissance;
    }

    public String getRole() {
        return role;
    }

    public String getGenre() {
        return genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNum_de_telephone() {
        return num_de_telephone;
    }




    //update constructor

    //to String



    public    UserSession(int id, String email, String password, String nom, String prenom, String date_de_naissance, String role
            , String genre, String adresse , String num_de_telephone)
    {
        this.id = id;

        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.date_de_naissance = date_de_naissance;
        this.role = role;
        this.genre = genre;
        this.adresse = adresse;
        this.num_de_telephone =num_de_telephone ;

    }

    public static UserSession getInstance(int id, String email, String password, String nom, String prenom, String date_de_naissance, String role
            , String genre, String adresse , String num_de_telephone) {
        if(instance == null) {
            instance = new UserSession(id,  email, password, nom, prenom, date_de_naissance, role
                    , genre, adresse, num_de_telephone);
        }
        return instance;

    }
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession has not been initialized.");
        }
        return instance;
    }

    public void cleanUserSession() {
        id = 0;
        email = null;
        password = null;
        nom= null;
        prenom = null;
       date_de_naissance = null;
       role = null;
        genre = null;
        adresse = null;
        num_de_telephone= null;


    }
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
