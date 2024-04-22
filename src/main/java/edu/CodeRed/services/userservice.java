package edu.CodeRed.services;

import edu.CodeRed.entities.user;
import edu.CodeRed.interfaces.IService;
import edu.CodeRed.tools.MyConnexion;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

import java.util.List;

public class userservice implements IService<user>{
    //Add user
    @Override
    public void addUser(user user) {
        String passwordencrypted = encrypt(user.getPassword());

        // 1. Prepare the SQL statement using a placeholder for each value
        String query = "INSERT INTO user (email, password,`nom`, prenom, date_de_naissance, genre, adresse, num_de_telephone) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            // 2. Set parameter values using appropriate user getters
            preparedStatement.setString(1, user.getEmail());

            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getNom());
            preparedStatement.setString(4, user.getPrenom());
            preparedStatement.setString(5, user.getDate_de_naissance());
            preparedStatement.setString(6, user.getGenre());
            preparedStatement.setString(7, user.getAdresse());
            preparedStatement.setString(8, user.getNum_de_telephone());


            // 3. Execute the update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }
    //Update User methode
    @Override
    public void UpdatUser(user user, int id) {


        String query = "UPDATE user " +
                "SET user_nom = ?, user_prenom = ?, user_date_de_naissance= ?, user_genre = ?, user_adresse = ?, user_num_de_telephone = ?, " +
                "user_roles = ?, user_email = ? , user_password = ?" +
                "WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query);
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(4, user.getDate_de_naissance());
            preparedStatement.setString(5, user.getGenre());
            preparedStatement.setString(6, user.getAdresse());
            preparedStatement.setString(7, user.getNum_de_telephone());

            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9,user.getPassword()) ;

            preparedStatement.setInt(10, id);

            preparedStatement.executeUpdate();
            System.out.println("User updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Delete User methode
    @Override
    public void DeleteUser(int id) {
        String query = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("User with the id = "+ id+" is deleted!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    //Retrieving data from database
    public List<user> getalluserdata() {
        List<user> list = new ArrayList<>();
        String query = "SELECT * FROM user";
        try {
            Statement srt = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = srt.executeQuery(query);
            while(rs.next()){
                user user = new user (

                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_de_naissance"),
                        rs.getString("genre"),
                        rs.getString("adresse"),
                        rs.getString("num_de_telephone"),

                        rs.getString("email"),
                        rs.getString("password"));


                list.add(user);
            }
            System.out.println("All users are added to the list!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    //Methode to encrypt the Username password
    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

