package org.example.dao;

import org.example.model.Transaction;

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

    private TransactionMapper transactionMapper = new TransactionMapper();

    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            transactions = transactionMapper.mapResultsetToList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        if (transaction.getCategory() == 0) {
            throw new IllegalArgumentException("The category is required for a transaction !");
        }
        String sql = "INSERT INTO transaction (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getLabel());
            statement.setDouble(2, transaction.getAmount());
            statement.setDate(3, (java.sql.Date) transaction.getDate());
            statement.setString(4, transaction.getType().name());
            statement.setInt(5, transaction.getCategory());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
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
                statement.setInt(5, transaction.getCategory());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    savedTransactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
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
            throw new RuntimeException();
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
            throw new RuntimeException();
        }
        return null;
    }
}