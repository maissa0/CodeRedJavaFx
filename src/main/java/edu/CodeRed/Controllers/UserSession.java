package edu.CodeRed.controllers;

public final class UserSession {

    private static UserSession instance;

    private int id;

    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String date_de_naissance;
    private String genre;

    private String num_de_telephone;
    private String role;

    // Private constructor to prevent instantiation from outside
    private UserSession(int id, String email, String password, String nom, String prenom, String date_de_naissance, String genre,
                        String num_de_telephone, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.date_de_naissance = date_de_naissance;
        this.genre = genre;
        this.num_de_telephone = num_de_telephone;
        this.role = role;
    }

    public static UserSession getInstance(int id, String email, String password, String nom, String prenom, String date_de_naissance, String genre,
                                          String num_de_telephone, String role) {
        if (instance == null) {
            instance = new UserSession(id, email, password, nom, prenom, date_de_naissance, genre, num_de_telephone, role);
        }
        return instance;
    }

    // Getter methods...

    public void cleanUserSession() {
        id = 0;
        email = null;
        password = null;
        nom = null;
        prenom = null;
        date_de_naissance = null;
        genre = null;
        num_de_telephone = null;
        role = null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", date_de_naissance='" + date_de_naissance + '\'' +
                ", genre='" + genre + '\'' +
                ", num_de_telephone='" + num_de_telephone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
