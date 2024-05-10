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

    public Journal(int id, int userId, Date date) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }

    public Journal(int userId, int caloriesJournal, Date date, List<Recette> recettes) {
        this.userId = userId;
        this.caloriesJournal = caloriesJournal;
        this.date = date;
        this.recettes = recettes;
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
        return this.caloriesJournal;
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
