package edu.CodeRed.entities;


import java.sql.Date;

public class Suivi_Activite {
    private int id;
    private int user_id;
    private Activite activite_id;
    private Date date;
    private int rep;

public Suivi_Activite()
{
}

    public Suivi_Activite(int user_id, Activite activite_id, Date date, int rep) {

        this.user_id = user_id;
        this.activite_id = activite_id;
        this.date = date;
        this.rep = rep;
    }

    public Suivi_Activite(Date date, int rep) {
        this.date = date;
        this.rep = rep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Activite getActivite_id() {
        return activite_id;
    }

    public void setActivite_id(Activite activite_id) {
        this.activite_id = activite_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    @Override
    public String toString() {
        return "Suivi_Activite{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", activite_id=" + activite_id +
                ", date=" + date +
                ", rep=" + rep +
                '}';
    }
}

