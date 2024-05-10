package edu.CodeRed.interfaces;

import java.util.Date;
import java.util.List;

public interface JService <T,Q>{


    public void addJournal(T t, List<Q> q);
    public void updateJournal(T t , List<Q> q);
    public List<T> getAllDataJournal(int id);
    public void deleteJournal(Date date);
    public T readJournal(int id);
}
