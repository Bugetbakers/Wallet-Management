package org.example.dao;

import org.example.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAO implements CrudOperation<Transaction> {
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
                Date date = resultSet.getTimestamp("date");
                String type = resultSet.getString("transactionType");
                int category = resultSet.getInt("id_category");

                Transaction.TransactionType transactionType;
                if (Transaction.TransactionType.CREDIT.name().equalsIgnoreCase(type)) {
                    transactionType = Transaction.TransactionType.CREDIT;
                } else {
                    transactionType = Transaction.TransactionType.DEBIT;
                }
                Transaction transaction = new Transaction(id, label, amount, date, transactionType, category);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        if (transaction.getCategory() == 0) {
            throw new IllegalArgumentException("The category is required for a transaction !");
        }
        String sql = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getLabel());
            statement.setDouble(2, transaction.getAmount());
            statement.setTimestamp(3, transaction.getDate() != null ? new Timestamp(transaction.getDate().getTime()) : null);
            statement.setString(4, transaction.getType() != null ? transaction.getType().name() : "DEBIT");
            statement.setInt(5, transaction.getCategory());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> savedTransactions = new ArrayList<>();
        String query = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (Transaction transaction : toSave) {
                statement.setString(1, transaction.getLabel());
                statement.setDouble(2, transaction.getAmount());
                statement.setTimestamp(3, transaction.getDate() != null ? new Timestamp(transaction.getDate().getTime()) : null);
                statement.setString(4, transaction.getType() != null ? transaction.getType().name() : "DEBIT");
                statement.setInt(5, transaction.getCategory());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            transaction.setId(generatedKeys.getInt(1));
                        }
                    }
                    savedTransactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return savedTransactions;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String query = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, toSave.getLabel());
            statement.setDouble(2, toSave.getAmount());
            statement.setTimestamp(3, toSave.getDate() != null ? new Timestamp(toSave.getDate().getTime()) : null);
            statement.setString(4, toSave.getType() != null ? toSave.getType().name() : "DEBIT");
            statement.setInt(5, toSave.getCategory());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        toSave.setId(generatedKeys.getInt(1));
                    }
                }
                return toSave;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return null;
    }
}