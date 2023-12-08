package org.example.Repository;

import lombok.Data;
import org.example.Model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionCrud implements CrudOperation<Transaction>{
    private Connection connection;
    public TransactionCrud (Connection connection) {
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

                Transaction transaction = new Transaction(id, label, amount, date, Transaction.TransactionType.type);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        List<Transaction> savedTransactions = new ArrayList<>();
        String query = "INSERT INTO transactions (id, sender_account_id, receiver_account_id, amount, currency, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Transaction transaction : toSave) {
                statement.setInt(1, transaction.getId());
                statement.setString(2, transaction.getLabel());
                statement.setDouble(4, transaction.getAmount());
                statement.setDate(5, (java.sql.Date) transaction.getDate());
                statement.setString(6, String.valueOf(transaction.getType()));

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
        String query = "INSERT INTO transactions (id, sender_account_id, receiver_account_id, amount, currency, description) VALUES (?, ?, ?, ?, ?, ?)";

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