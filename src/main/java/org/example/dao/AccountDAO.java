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
                Account account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setName(resultSet.getString("name"));
                account.setBalance(resultSet.getDouble("balance"));
                account.setType(Account.AccountType.valueOf(resultSet.getString("type")));
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Transaction> getTransactionsForAccount(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE id_account = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setLabel(resultSet.getString("label"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setDate(resultSet.getTimestamp("date"));
                transaction.setType(Transaction.TransactionType.valueOf(resultSet.getString("transactionType")));
                transaction.setCategory(resultSet.getInt("id_category"));
                transactions.add(transaction);
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
            String sql = "INSERT INTO account (name, balance, currency, type) VALUES (?,?,?,?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Account account : toSave) {
                    statement.setString(1, account.getName());
                    statement.setDouble(2, account.getBalance());
                    statement.setInt(3, account.getCurrency() != null ? account.getCurrency().getId() : 0);
                    statement.setString(4, account.getType().name());
                    statement.addBatch();
                }
                statement.executeBatch();
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
    public Account save(Account toSave) {
        String sql = "INSERT INTO account (name, balance, currency, type) VALUES (?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, toSave.getName());
            statement.setDouble(2, toSave.getBalance());
            statement.setInt(3, toSave.getCurrency() != null ? toSave.getCurrency().getId() : 0);
            statement.setString(4, toSave.getType().name());
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
