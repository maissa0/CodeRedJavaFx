package edu.CodeRed.entities;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Journal {
    private int id;
    private int userId;
    private int caloriesJournal;
    private Date date;
    private List<Recette> recettes;

    // Constructors
    public Journal() {
    }

    public Journal(int userId, Date date) {
        this.userId = userId;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCaloriesJournal() {
        return calculateTotalCalories();
    }

    public void setCaloriesJournal(int caloriesJournal) {
        this.caloriesJournal = caloriesJournal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Recette> getRecettes() {
        return recettes;
    }

    public void setRecettes(List<Recette> recettes) {
        this.recettes = recettes;
        // Recalculate caloriesJournal
        setCaloriesJournal();
    }

    private void setCaloriesJournal() {
        if (recettes != null) {
            int totalCalories = 0;

            // Loop through all the Recettes associated with this Journal and sum their calorieRecette
            for (Recette recette : recettes) {
                totalCalories += recette.getCalorieRecette();
            }

            this.caloriesJournal = totalCalories;
        }
    }

    public int calculateTotalCalories() {
        int totalCalories = 0;

        if (recettes != null) {
            // Loop through all the Recettes associated with this Journal and sum their calorieRecette
            for (Recette recette : recettes) {
                totalCalories += recette.getCalorieRecette();
            }
        }

        return totalCalories;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", userId=" + userId +
                ", caloriesJournal=" + caloriesJournal +
                ", date=" + date +
                '}';
    }
}
