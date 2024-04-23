package edu.CodeRed.services;


import edu.CodeRed.entities.Journal;
import edu.CodeRed.entities.Recette;
import edu.CodeRed.interfaces.JService;
import edu.CodeRed.interfaces.RService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.CodeRed.tools.MyConnexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JournalService implements JService<Journal, Recette> {
    @Override
    public void addJournal(Journal journal, List<Recette> recettes) {
        String query = "INSERT INTO journal (id_user_id, calories_journal, date) VALUES (?, ?, ?)";
        try {
            try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, journal.getUserId());
                ps.setInt(2, journal.getCaloriesJournal());
                ps.setDate(3, new java.sql.Date(journal.getDate().getTime()));
                ps.executeUpdate();
                System.out.println(journal.getCaloriesJournal());
                System.out.println(journal.calculateTotalCalories());

                // Get the auto-generated ID of the newly inserted journal
                int journalId;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        journalId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve auto-generated keys.");
                    }
                }

                // Insert into journal_recette table to establish many-to-many relationship
                String insertJournalRecetteQuery = "INSERT INTO journal_recette (journal_id, recette_id) VALUES (?, ?)";
                try (PreparedStatement ps1 = MyConnexion.getInstance().getCnx().prepareStatement(insertJournalRecetteQuery)) {
                    System.out.println(journalId);

                    for (Recette recette : recettes) {
                        // Insert the relationship
                        ps1.setInt(1, journalId);
                        ps1.setInt(2, recette.getId());
                        ps1.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Journal added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateJournal(Journal journal, List<Recette> recettes) {
        String updateJournalQuery = "UPDATE journal SET id_user_id = ?, calories_journal = ?, date = ? WHERE id = ?";
        String deleteJournalRecetteQuery = "DELETE FROM journal_recette WHERE journal_id = ?";
        try {
            try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(updateJournalQuery)) {
                ps.setInt(1, journal.getUserId());
                ps.setInt(2, journal.getCaloriesJournal());
                ps.setDate(3, new java.sql.Date(journal.getDate().getTime()));
                ps.setInt(4, journal.getId());
                ps.executeUpdate();

                try (PreparedStatement psDelete = MyConnexion.getInstance().getCnx().prepareStatement(deleteJournalRecetteQuery)) {
                    psDelete.setInt(1, journal.getId());
                    psDelete.executeUpdate();
                }

                // Insert into journal_recette table to establish many-to-many relationship
                String insertJournalRecetteQuery = "INSERT INTO journal_recette (journal_id, recette_id) VALUES (?, ?)";
                try (PreparedStatement ps1 = MyConnexion.getInstance().getCnx().prepareStatement(insertJournalRecetteQuery)) {
                    System.out.println(journal.getId());

                    for (Recette recette : recettes) {
                        // Insert the relationship
                        ps1.setInt(1, journal.getId());
                        ps1.setInt(2, recette.getId());
                        ps1.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Journal updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Journal> getAllDataJournal() {
        List<Journal> journals = new ArrayList<>();
        String query = "SELECT * FROM journal";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Journal journal = new Journal();
                    int journalId = rs.getInt("id");
                    journal.setId(journalId);
                    journal.setDate(rs.getDate("date"));
                    journal.setUserId(rs.getInt("id_user_id"));
                    int caloriesJournal = rs.getInt("calories_journal");
                    journal.setCaloriesJournal(caloriesJournal);
                    journal.setRecettes(getRecettesForJournal(journalId));
                    journals.add(journal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return journals;
    }

    private List<Recette> getRecettesForJournal(int journalId) {
        List<Recette> recettes = new ArrayList<>();
        String query = "SELECT r.* FROM recette r " +
                "INNER JOIN journal_recette jr ON r.id = jr.recette_id " +
                "WHERE jr.journal_id = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            ps.setInt(1, journalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Recette recette = new Recette();
                    recette.setId(rs.getInt("id"));
                    recette.setNom(rs.getString("nom"));
                    recette.setCategorie(rs.getString("categorie"));
                    recette.setImage(rs.getString("image"));
                    recette.setDescription(rs.getString("description"));
                    recette.setCalorieRecette(rs.getInt("calorie_recette"));
                    recettes.add(recette);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recettes;
    }






    @Override
    public void deleteJournal(Date date) {
        String deleteJournalQuery = "DELETE FROM journal WHERE date = ?";
        try {
            try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(deleteJournalQuery)) {
                ps.setDate(1, new java.sql.Date(date.getTime()));
                ps.executeUpdate();
                System.out.println("Journal deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Journal readJournal(int id) {
        Journal journal = null;
        String query = "SELECT * FROM journal WHERE id = ?";
        try (PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    journal = new Journal();
                    journal.setId(rs.getInt("id"));
                    journal.setDate(rs.getDate("date"));
                    journal.setUserId(rs.getInt("id_user_id"));
                    int caloriesJournal = rs.getInt("calories_journal");
                    journal.setCaloriesJournal(caloriesJournal);
                    journal.setRecettes(getRecettesForJournal(id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return journal;
    }

}
