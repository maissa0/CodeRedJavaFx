package com.example.projetpi.interfaces;

import com.example.projetpi.entities.Activite;
import com.example.projetpi.entities.Suivi_Activite;

import java.util.List;

public interface ISuivi_Activite {

    public void addSuivi(Suivi_Activite suivi_activite ,int activiteId,int user_id);
    public void updateSuivi(Suivi_Activite suivi_activite ,int activiteId,int user_id);
    public void supprimerSuivi(Suivi_Activite suivi_activite);
    public Suivi_Activite readSuivi(int id);
    public List<Suivi_Activite> afficherAllSuivi();

}
