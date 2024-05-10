package edu.CodeRed.interfaces;

import java.util.List;

public interface RService <T,Q>{


    public void addRecette(T t, List<Q> q);
    public void updateRecette(T t , List<Q> q);
    public List<T> getAllDataRecette();
    public void deleteRecette(int id);
    public T readRecette(String name);
}
