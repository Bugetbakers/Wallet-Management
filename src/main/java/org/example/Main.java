package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static Connection connection;
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = System.getenv("url");
                String userName = System.getenv("user");
                String password = System.getenv("password");
                if (url == null || userName == null || password == null) {
                    throw new IllegalStateException("Please define the url, username and password in the environment variable");
                }
                connection = DriverManager.getConnection(url, userName, password);
                System.out.println("Connection Successfully !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static void main(String[] args) {
        System.out.println("Connected");
        getConnection();
    }
}