package org.example;

import org.example.config.ConnectionFactory;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            System.out.println("Connected successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        } finally {
            ConnectionFactory.shutdown();
        }
    }
}