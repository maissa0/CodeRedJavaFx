package edu.CodeRed.interfaces;

 import edu.CodeRed.entities.Activite;

import java.util.List;

public interface IActivite {

    public void addEntity(Activite activite);
    public void updateActivite(Activite activite);
    public void supprimerActivite(Activite a);
    public Activite readActivite(int i);
    public List<Activite> afficherAllActivite();
    public Activite readActiviteByName(String nom);
    public List<Activite> SearchActivite(String searchCriteria);}
