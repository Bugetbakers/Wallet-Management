package org.example.dao;

import org.example.model.Transaction;
import org.example.model.Transaction.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAO implements CrudOperation<Transaction>{
    private Connection connection;
    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String label = resultSet.getString("label");
                double amount = resultSet.getDouble("amount");
                Date date = resultSet.getDate("date");
                String type = resultSet.getString("type");

                Transaction transaction = new Transaction(id, label, amount, date, TransactionType.type);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        if (transaction.getCategory() == null) {
            throw new IllegalArgumentException("The category is required for a transaction !")
        }
        String sql = "INSERT INTO transaction (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getLabel());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, (java.sql.Date) transaction.getDate());
            statement.setString(4, transaction.getType().name());
            statement.setInt(5, transaction.getCategory().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> savedTransactions = new ArrayList<>();
        String query = "INSERT INTO transactions (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Transaction transaction : toSave) {
                statement.setString(1, transaction.getLabel());
                statement.setDouble(2, transaction.getAmount());
                statement.setDate(3, (java.sql.Date) transaction.getDate());
                statement.setString(4, transaction.getType().name());
                statement.setInt(5, transaction.getCategory().getId());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    savedTransactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedTransactions;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String query = "INSERT INTO transactions (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, toSave.getId());
            statement.setString(2, toSave.getLabel());
            statement.setDouble(4, toSave.getAmount());
            statement.setDate(5, (java.sql.Date) toSave.getDate());
            statement.setString(6, String.valueOf(toSave.getType()));

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction delete(Transaction toDelete) {
        String query = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, toDelete.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}