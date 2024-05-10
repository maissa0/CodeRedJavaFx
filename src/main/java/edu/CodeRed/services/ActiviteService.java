package edu.CodeRed.services;


import edu.CodeRed.entities.Activite;

import edu.CodeRed.interfaces.IActivite;
import edu.CodeRed.tools.MyConnexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActiviteService implements IActivite {

    @Override
    public void addEntity(Activite activite) {
        String requette = "INSERT INTO activite (type,nom,nbr_cal,description,image,video) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, activite.getType());
            pst.setString(2, activite.getNom());
            pst.setInt(3, activite.getNbr_cal());
            pst.setString(4, activite.getDescription());
            pst.setString(5, activite.getImage());
            pst.setString(6, activite.getVideo());
            pst.executeUpdate();
            System.out.println("Activity added");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void updateActivite(Activite a) {
        try {
            String query = "UPDATE activite SET type=?, nom=?, nbr_cal=?, description=?, image=?, video=? WHERE id=?";
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, a.getType());
            pst.setString(2, a.getNom());
            pst.setInt(3, a.getNbr_cal());
            pst.setString(4, a.getDescription());
            pst.setString(5, a.getImage());
            pst.setString(6, a.getVideo());
            pst.setInt(7,a.getId());
            pst.executeUpdate();

            System.out.println("Activity updated !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void supprimerActivite(Activite c)  {
        try {
            String req = "DELETE FROM activite WHERE id=" + c.getId();
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.executeUpdate(req);
            System.out.println("Activity deleted !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Activite readActivite(int id) {
        Activite a =new Activite();
        String requette = "SELECT * FROM activite WHERE id=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                a.setId(rs.getInt("id"));
                a.setType(rs.getString("type"));
                a.setNom(rs.getString("nom"));
                a.setNbr_cal(rs.getInt("nbr_cal"));
                a.setDescription(rs.getString("description"));
                a.setImage(rs.getString("image"));
                a.setVideo(rs.getString("video"));
                return a;
            } else {
                System.out.println("No Activity found with ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Activite> afficherAllActivite()  {
        List<Activite> list = new ArrayList<>();
        try {
            String requette = "Select * from activite";
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);


            ResultSet RS = pst.executeQuery(requette);
            while (RS.next()) {
                Activite a = new Activite();
                a.setId(RS.getInt("id"));
                a.setType(RS.getString(2));
                a.setNom(RS.getString(3));
                a.setNbr_cal(RS.getInt(4));
                a.setDescription(RS.getString(5));
                a.setImage(RS.getString(6));
                a.setVideo(RS.getString(7));
                list.add(a);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;

    }

    @Override
    public List<Activite> SearchActivite(String searchCriteria)
    {
        List<Activite> list = new ArrayList<>();
        String query = "SELECT * FROM activite WHERE id = ? OR nom LIKE ? OR type LIKE ? OR nbr_cal = ? OR description LIKE ?";

        try (PreparedStatement statement = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            // Set the parameters
            // Assuming searchCriteria can be any type of string, integer, or double value
            int searchInt = 0;
            double searchDouble = 0.0;
            boolean isNumeric = false;

            // Check if searchCriteria is numeric
            try {
                searchInt = Integer.parseInt(searchCriteria);
                isNumeric = true;
            } catch (NumberFormatException e) {
                try {
                    searchDouble = Double.parseDouble(searchCriteria);
                    isNumeric = true;
                } catch (NumberFormatException ex) {
                    // If it's neither an integer nor double, keep isNumeric as false
                }
            }

            statement.setInt(1, isNumeric ? searchInt : 0); // Set id parameter
            statement.setString(2, "%" + searchCriteria + "%"); // Set nom parameter
            statement.setString(3, "%" + searchCriteria + "%"); // Set type parameter
            if (isNumeric) {
                if (searchInt == 0 && searchDouble != 0.0) {
                    statement.setDouble(4, searchDouble); // Set nbr_cal parameter for doubles
                } else {
                    statement.setInt(4, searchInt); // Set nbr_cal parameter for integers
                }
            } else {
                statement.setInt(4, 0); // Default value if not numeric
            }
            statement.setString(5, "%" + searchCriteria + "%"); // Set description parameter

            // Execute the query
            ResultSet RS = statement.executeQuery();

            // Process the results
            while (RS.next()) {
                Activite a = new Activite();
                a.setId(RS.getInt("id"));
                a.setType(RS.getString(2));
                a.setNom(RS.getString(3));
                a.setNbr_cal(RS.getInt(4));
                a.setDescription(RS.getString(5));
                a.setImage(RS.getString(6));
                a.setVideo(RS.getString(7));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public Activite readActiviteByName(String nom) {
        Activite a =new Activite();
        String requette = "SELECT * FROM activite WHERE nom=?";
        try {
            PreparedStatement pst = MyConnexion.getInstance().getCnx().prepareStatement(requette);
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                a.setId(rs.getInt("id"));
                a.setType(rs.getString("type"));
                a.setNom(rs.getString("nom"));
                a.setNbr_cal(rs.getInt("nbr_cal"));
                a.setDescription(rs.getString("description"));
                a.setImage(rs.getString("image"));
                a.setVideo(rs.getString("video"));
                return a;
            } else {
                System.out.println("No Activity found with Name " + nom);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
