package org.example.Repository;

import org.example.Model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyCrud implements CrudOperation<Currency>{
    private Connection connection;
    public CurrencyCrud (Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Currency> findAll() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM currency";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                Currency currency = new Currency(id, code, name, country);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                    statement.setString(4, currency.getCountry());

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
    public Currency save(Currency toSave) {
        String sql = "INSERT INTO currency (id, code, name, country) VALUES (?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, toSave.getId());
            statement.setString(2, toSave.getCode());
            statement.setString(3, toSave.getName());
            statement.setString(4, toSave.getCountry());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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