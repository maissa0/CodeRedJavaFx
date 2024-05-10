package edu.CodeRed.interfaces;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;
public interface IService<T> {
    void addUser(T t) throws SQLException, IOException;
    void UpdatUser(T t);
    void DeleteUser(int id);
    List<T> getalluserdata();
}
