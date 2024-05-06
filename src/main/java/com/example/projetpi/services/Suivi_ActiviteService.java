package com.example.projetpi.services;

import com.example.projetpi.entities.Activite;
import com.example.projetpi.entities.Suivi_Activite;
import com.example.projetpi.interfaces.ISuivi_Activite;
import com.example.projetpi.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Suivi_ActiviteService implements ISuivi_Activite {
    public ActiviteService activiteService = new ActiviteService();
    @Override
    public void addSuivi(Suivi_Activite suivi_activite, int activiteId, int UserId) {
        String requette = "INSERT INTO suivi_activite (user_id,activite_id,date,rep) VALUES (?,?,?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requette, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, UserId);
            pst.setInt(2, activiteId);
            pst.setDate(3,suivi_activite.getDate());
            pst.setInt(4, suivi_activite.getRep());

            pst.executeUpdate();
            System.out.println("Suivi added");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void updateSuivi(Suivi_Activite a , int activiteId, int UserId) {
        try {
            String query = "UPDATE suivi_activite SET user_id=?, activite_id=?, date=?, rep=? WHERE id=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1,UserId);
            pst.setInt(2, activiteId);
            pst.setDate(3, a.getDate());
            pst.setInt(4, a.getRep());
            pst.setInt(5,a.getId());
            pst.executeUpdate();

            System.out.println("Suivi updated !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void supprimerSuivi(Suivi_Activite c)  {
        try {
            String req = "DELETE FROM suivi_activite WHERE id=" + c.getId();
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            pst.executeUpdate(req);
            System.out.println("Suivi deleted !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public Suivi_Activite readSuivi(int id) {
        Suivi_Activite a = new Suivi_Activite();
        String requette = "SELECT * FROM suivi_activite WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requette);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                a.setId(rs.getInt("id"));
                a.setUser_id(rs.getInt("user_id"));
                Activite ac = activiteService.readActivite(rs.getInt("activite_id"));
                a.setActivite_id(ac);
                a.setDate(rs.getDate("date"));
                a.setRep(rs.getInt("rep"));
                return a;
            } else {
                System.out.println("No Suivi found with ID " + id);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }

    }

    @Override
    public List<Suivi_Activite> afficherAllSuivi()  {
        List<Suivi_Activite> list = new ArrayList<>();
        try {
            String requette = "Select * from suivi_avtivite";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requette);


            ResultSet RS = pst.executeQuery(requette);
            while (RS.next()) {
                Suivi_Activite a = new Suivi_Activite();
                a.setId(RS.getInt(1));
                a.setUser_id(RS.getInt(2));
                Activite ac = activiteService.readActivite(RS.getInt(3));
                a.setActivite_id(ac);
                a.setDate(RS.getDate(4));
                a.setRep(RS.getInt(5));
                list.add(a);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;

    }
}
