package org.example.dao;

import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO implements CrudOperation<Currency>{
    private Connection connection;
    public CurrencyDAO(Connection connection) {
        this.connection = connection;
    }

    private CurrencyMapper currencyMapper = new CurrencyMapper();

    @Override
    public List<Currency> findAll() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM currency";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            currencies = currencyMapper.mapResultSetToList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return currencies;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSave) {
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO currency (id, code, name, country) VALUES (?,?,?,?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Currency currency : toSave) {
                    statement.setInt(1, currency.getId());
                    statement.setString(2, currency.getCode());
                    statement.setString(3, currency.getName());

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
    public Currency save(Currency toSave) {
        String sql = "INSERT INTO currency (id, code, name, country) VALUES (?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, toSave.getId());
            statement.setString(2, toSave.getCode());
            statement.setString(3, toSave.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public Currency delete(Currency toDelete) {
        try {
            String sql = "DELETE FROM currency WHERE id = ?";
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