package com.example.projetpi.interfaces;

import com.example.projetpi.entities.Activite;

import java.util.List;

public interface IActivite {

    public void addEntity(Activite activite);
    public void updateActivite(Activite activite);
    public void supprimerActivite(Activite a);
    public Activite readActivite(int i);
    public List<Activite> afficherAllActivite();

    public List<Activite> SearchActivite(String searchCriteria);}
