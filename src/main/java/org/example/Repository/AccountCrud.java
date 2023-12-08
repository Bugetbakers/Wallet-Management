package org.example.Repository;

import org.example.Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountCrud implements CrudOperation<Account>{
    private Connection connection;
    public AccountCrud (Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                String transaction = resultSet.getString("transaction");
                String currency = resultSet.getString("currency");
                String type = resultSet.getString("type");
                Account account = new Account(id, name, balance, transaction, currency, type);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO account (id, name, balance, currency, password) VALUES (?,?,?,?,?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Account accounts : toSave) {
                    statement.setInt(1, accounts.getId());
                    statement.setString(2, accounts.getName());
                    statement.setDouble(3, accounts.getBalance());
                    statement.setString(4, String.valueOf(accounts.getCurrency()));
                    statement.setString(5, accounts.getTransactions().toString());
                    statement.setString(5, String.valueOf(accounts.getType()));

                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    @Override
    public Account save(Account toSave) {
        String sql = "INSERT INTO account (id, name, balance, currency, password) VALUES (?,?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, toSave.getId());
            statement.setString(2, toSave.getName());
            statement.setString(4, String.valueOf(toSave.getCurrency()));
            statement.setString(5, toSave.getTransactions().toString());
            statement.setString(5, String.valueOf(toSave.getType()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account delete(Account toDelete) {
        try {
            String sql = "DELETE FROM account WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, toDelete.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return toDelete;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
