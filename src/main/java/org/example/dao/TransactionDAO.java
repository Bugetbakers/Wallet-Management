package org.example.dao;

import org.example.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements CrudOperation<Transaction> {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    private TransactionMapper transactionMapper = new TransactionMapper();

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            statement.setTimestamp(3, new Timestamp(transaction.getDate().getTime()));
            statement.setString(4, transaction.getType().name());
            statement.setInt(5, transaction.getCategory());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        String sql = "INSERT INTO transaction (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (Transaction transaction : toSave) {
                statement.setString(1, transaction.getLabel());
                statement.setDouble(2, transaction.getAmount());
                statement.setTimestamp(3, new Timestamp(transaction.getDate().getTime()));
                statement.setString(4, transaction.getType().name());
                statement.setInt(5, transaction.getCategory());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException(e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        return toSave;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String sql = "INSERT INTO transaction (label, amount, date, type, category_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getLabel());
            statement.setDouble(2, toSave.getAmount());
            statement.setTimestamp(3, new Timestamp(toSave.getDate().getTime()));
            statement.setString(4, toSave.getType().name());
            statement.setInt(5, toSave.getCategory());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Transaction delete(Transaction toDelete) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, toDelete.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return toDelete;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}