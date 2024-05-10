package edu.CodeRed.interfaces;

import java.util.List;

public interface IServices<T,Q>{

    public void addEntity(T t);
    public void updateEntity(T t);
    public List<T> getAllData();
    public void deleteEntity(int id);
    public T readEntity(int id);
}
