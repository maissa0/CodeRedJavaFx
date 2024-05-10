package edu.CodeRed.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private LocalDate dateCmd;
    private String etatCmd;
    private int quantiteCmd;
    private double total;

    public Commande(int id, LocalDate dateCmd, String etatCmd, int quantiteCmd, double total) {
        this.id = id;
        this.dateCmd = dateCmd;
        this.etatCmd = etatCmd;
        this.quantiteCmd = quantiteCmd;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateCmd() {
        return dateCmd;
    }

    public void setDateCmd(LocalDate dateCmd) {
        this.dateCmd = dateCmd;
    }

    public String getEtatCmd() {
        return etatCmd;
    }

    public void setEtatCmd(String etatCmd) {
        this.etatCmd = etatCmd;
    }

    public int getQuantiteCmd() {
        return quantiteCmd;
    }

    public void setQuantiteCmd(int quantiteCmd) {
        this.quantiteCmd = quantiteCmd;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", dateCmd=" + dateCmd +
                ", etatCmd='" + etatCmd + '\'' +
                ", quantiteCmd=" + quantiteCmd +
                ", total=" + total +
                '}';
    }

}
