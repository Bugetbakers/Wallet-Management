package org.example.dao;

import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements CrudOperation<Account> {
    private Connection connection;

    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    private AccountMapper accountMapper = new AccountMapper();

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            accounts = accountMapper.mapResultSetToList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                        new ArrayList<>(),
                        new Currency(
                                resultSet.getInt("currency_id"),
                                resultSet.getString("currency_code"),
                                resultSet.getString("currency_name"),
                                resultSet.getString("currency_country")
                        ),
                        Account.AccountType.valueOf(resultSet.getString("type")),
                        resultSet.getObject("created_at", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                java.sql.Timestamp date = resultSet.getTimestamp("date");
                String type = resultSet.getString("type");
                int category = resultSet.getInt("category");
                Transaction.TransactionType txType = Transaction.TransactionType.CREDIT.name().equals(type)
                        ? Transaction.TransactionType.CREDIT
                        : Transaction.TransactionType.DEBIT;
                Transaction transaction = new Transaction(transactionId, label, amount, date, txType, category);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        String sql = "INSERT INTO account (id, name, balance, currency_id, type, created_at) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (Account account : toSave) {
                statement.setInt(1, account.getId());
                statement.setString(2, account.getName());
                statement.setDouble(3, account.getBalance());
                statement.setInt(4, account.getCurrency().getId());
                statement.setString(5, account.getType().name());
                statement.setObject(6, account.getCreatedAt());
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
    public Account save(Account toSave) {
        String sql = "INSERT INTO account (id, name, balance, currency_id, type, created_at) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, toSave.getId());
            statement.setString(2, toSave.getName());
            statement.setDouble(3, toSave.getBalance());
            statement.setInt(4, toSave.getCurrency().getId());
            statement.setString(5, toSave.getType().name());
            statement.setObject(6, toSave.getCreatedAt());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Account delete(Account toDelete) {
        String sql = "DELETE FROM account WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
