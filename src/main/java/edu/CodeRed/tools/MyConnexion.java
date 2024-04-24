package edu.CodeRed.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnexion {

    private String url="jdbc:mysql://127.0.0.1:3306/opti'health";
    private String login="root";
    private String pwd="";
    Connection cnx;
    public static MyConnexion instance;

    public MyConnexion(){
        try {
            cnx = DriverManager.getConnection(url,login,pwd);
            System.out.println("Connected To DATABASE !");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnexion getInstance() {
        if (instance == null){
            instance=new MyConnexion();
        }
        return instance;
    }

    public Connection getCon() {
       return  cnx;
    }
}