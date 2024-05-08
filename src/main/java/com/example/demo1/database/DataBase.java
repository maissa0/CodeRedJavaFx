package com.example.demo1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/optihealth";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
