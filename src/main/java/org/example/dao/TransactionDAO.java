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
    public List<Transaction> findAll() {
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
        String sql = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, transaction.getLabel());
            statement.setDouble(2, transaction.getAmount());
            statement.setTimestamp(3, new java.sql.Timestamp(transaction.getDate().getTime()));
            statement.setString(4, transaction.getType().name());
            statement.setInt(5, transaction.getCategory());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (Transaction transaction : toSave) {
                    statement.setString(1, transaction.getLabel());
                    statement.setDouble(2, transaction.getAmount());
                    statement.setTimestamp(3, new java.sql.Timestamp(transaction.getDate().getTime()));
                    statement.setString(4, transaction.getType().name());
                    statement.setInt(5, transaction.getCategory());
                    statement.addBatch();
                }
                statement.executeBatch();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                int index = 0;
                while (generatedKeys.next()) {
                    toSave.get(index).setId(generatedKeys.getInt(1));
                    index++;
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Transaction save(Transaction toSave) {
        String query = "INSERT INTO transactions (label, amount, date, transactionType, id_category) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, toSave.getLabel());
            statement.setDouble(2, toSave.getAmount());
            statement.setTimestamp(3, new java.sql.Timestamp(toSave.getDate().getTime()));
            statement.setString(4, toSave.getType().name());
            statement.setInt(5, toSave.getCategory());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                toSave.setId(generatedKeys.getInt(1));
            }
            return toSave;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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