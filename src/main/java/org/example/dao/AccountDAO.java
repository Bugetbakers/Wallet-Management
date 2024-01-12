package org.example.dao;

import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements CrudOperation<Account>{
    private Connection connection;
    public AccountDAO(Connection connection) {
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
                Account account = new Account();
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return accounts;
    }

    public Account findById(int accountId) {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("balance"),
                        List.of(new Transaction()),
                        new Currency(
                                resultSet.getInt("id"),
                                resultSet.getString("code"),
                                resultSet.getString("name")
                        ),
                        Account.AccountType.valueOf(resultSet.getString("type")),
                        resultSet.getTimestamp("date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    public List<Transaction> getTransactionsForAccount(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                int transactionId = resultSet.getInt("id");
                String label = resultSet.getString("label");
                double amount = resultSet.getDouble("amount");
                java.sql.Date date = resultSet.getDate("date");
                String type = resultSet.getString("type");
                int category = resultSet.getInt("category");
                if (Transaction.TransactionType.DEBIT.toString() == type) {
                    Transaction transaction = new Transaction(transactionId, label, amount, date, Transaction.TransactionType.DEBIT, category);
                    transactions.add(transaction);
                } else {
                    Transaction transaction = new Transaction(transactionId, label, amount, date, Transaction.TransactionType.CREDIT, category);
                    transactions.add(transaction);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
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
                throw new RuntimeException();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
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
            throw new RuntimeException();
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
