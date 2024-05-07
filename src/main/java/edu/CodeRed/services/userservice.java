package edu.CodeRed.services;

import com.restfb.types.User;
import edu.CodeRed.entities.user;
import edu.CodeRed.interfaces.IService;
import edu.CodeRed.tools.MyConnexion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class userservice implements IService<user>{
    //Add user
    private Connection cnx;
    public userservice(){ cnx= MyConnexion.getInstance().getCnx();
    }
    @Override
    public void addUser(user user) {
        // 1. Prepare the SQL statement using a placeholder for each value
        String sql = "INSERT INTO `user` (`email`, `password`,`nom`, `prenom`, `date_de_naissance`, `role`,`genre`, `adresse`, `num_de_telephone`) VALUES (?,  ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(sql)) {
            // 2. Set parameter values using appropriate user getters
            pstm.setString(1, user.getEmail());

            pstm.setString(2, user.getPassword());
            pstm.setString(3, user.getNom());
            pstm.setString(4, user.getPrenom());
            pstm.setString(5, user.getDate_de_naissance());
            pstm.setString(6, user.getRole());
            pstm.setString(7, user.getGenre());
            pstm.setString(8, user.getAdresse());
            pstm.setString(9, user.getNum_de_telephone());


            // 3. Execute the update
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public void UpdatUser(user user) {
        String query = "UPDATE user SET email=?, password=?, nom=?, prenom=?, date_de_naissance=?,genre=?,Adresse=?,num_de_telephone=?,role=? WHERE id=?";

        try {
            // Create a prepared statement
            PreparedStatement preparedStatement = cnx.prepareStatement(query);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3, user.getNom());
            preparedStatement.setString(4, user.getPrenom());
            preparedStatement.setString(5, user.getDate_de_naissance());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setString(7, user.getGenre());
            preparedStatement.setString(8, user.getAdresse());
            preparedStatement.setString(9, user.getNum_de_telephone());

            preparedStatement.setInt(10, user.getId());

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No User found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
        } catch (NullPointerException e) {
            System.out.println("Connection is not initialized: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
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
    public  List<user> getalluserdata() {
        List<user> list = new ArrayList<>();
        String query = "SELECT * FROM user";
        try {
            Statement srt = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = srt.executeQuery(query);
            while(rs.next()){
                user User = new user ();
                        User.setId(rs.getInt(1));
                User.setEmail(rs.getString("email"));
                User.setPassword(rs.getString("password"));
                User.setNom(rs.getString("nom"));
                User.setPrenom(rs.getString("prenom"));
                User.setDate_de_naissance(rs.getString("date_de_naissance"));
                User.setRole(rs.getString("role"));
                User.setGenre(rs.getString("genre"));
                User.setAdresse(rs.getString("adresse"));
                User.setNum_de_telephone(rs.getString("num_de_telephone"));


                list.add(User);
            }
            System.out.println("All users are added to the list!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
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
    public void DeleteUserr(user user) {
        String query = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            System.out.println("User with the id = " + user.getId() + " is deleted!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void DeleteEntityWithConfirmation(user User) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Suppression de logement");
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer ce logement?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion, proceed with deletion
            DeleteUserr(User);
        }
    }


    //Methode to login
    public user loginUser(String Email, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        String encryptedPassword = encrypt(password); // Assuming encrypt is a secure hashing function

        try (PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            preparedStatement.setString(1,Email);
            preparedStatement.setString(2, encryptedPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User login successful, create a user object and return it
                    int userId = resultSet.getInt("id");
                    String userEmail = resultSet.getString("email");
                    String userNom = resultSet.getString("nom");
                    String userPrenom = resultSet.getString("prenom");
                    String userDateDeNaissance = resultSet.getString("date_de_naissance");
                    String userRole = resultSet.getString("role");
                    String userGenre = resultSet.getString("genre");
                    String userAdresse = resultSet.getString("adresse");
                    String userNumDeTelephone = resultSet.getString("num_de_telephone");

                    // Consider using a builder pattern for user object creation (optional)
                    return new user(userId, userEmail, userNom, userPrenom, userDateDeNaissance, userRole, userGenre, userAdresse, userNumDeTelephone);
                } else {
                    return null; // Login failed
                }
            }
        }
    }


    public void updateforgottenpassword(String Email, String Password) {
        String passwordencrypted = encrypt(Password);

        String query = "UPDATE user " +
                "SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = MyConnexion.getInstance().getCon().prepareStatement(query);
            preparedStatement.setString(1, passwordencrypted);
            preparedStatement.setString(2, Email);
            preparedStatement.executeUpdate();
            System.out.println("Password updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // isUsernameTaken/isEmailTaken methodes to check the uniqueness of a user

    public boolean isEmailTaken(String Email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        PreparedStatement preparedStatement = MyConnexion.getInstance().getCon().prepareStatement(query);
        preparedStatement.setString(1, Email);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        }
    }


    public List<user> triNom() {

        List<user> list1 = new ArrayList<>();
        List<user> list2 = getalluserdata();

        list1 = list2.stream().sorted((o1, o2) -> o1.getNom().compareTo(o2.getNom())).collect(Collectors.toList());
        return list1;

    }


    public List<user> trimail() {

        List<user> list1 = new ArrayList<>();
        List<user> list2 = getalluserdata();

        list1 = list2.stream().sorted((o1, o2) -> o1.getEmail().compareTo(o2.getEmail())).collect(Collectors.toList());
        return list1;

    }
}


