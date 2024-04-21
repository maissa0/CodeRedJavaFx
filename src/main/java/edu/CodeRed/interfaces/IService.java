package edu.CodeRed.interfaces;

import edu.CodeRed.entities.Ingredient;

import java.util.List;

public interface IService <T,Q>{

    public void addEntity(T t);
    public void addEntitylist(T t,List<Q> q);
    public void updateEntity(T t);
    public List<T> getAllData();
    public void deleteEntity(int id);
    public T readEntity(int id);
}
