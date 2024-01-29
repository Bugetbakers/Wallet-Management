package org.example.dao;

import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;

import java.lang.reflect.Field;
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
            String sql = "INSERT INTO account (id, name, balance, currency, type) VALUES (?,?,?,?,?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Account account : toSave) {
                    setPreparedStatementParams(statement, account);
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

    private void setPreparedStatementParams(PreparedStatement statement, Account account) {
        try {
            Field[] fields = Account.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(account);
                statement.setObject(i + 1, value);
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
