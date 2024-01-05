package org.example.dao;

import org.example.model.EchangeRate;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EchangeRateDAO {
    private Connection connection;
    public EchangeRateDAO(Connection connection) {
        this.connection = connection;
    }

    public List<EchangeRate> getExchangeRatesForDate(LocalDateTime date) {
        List<EchangeRate> echangeRates = new ArrayList<>();
        String sql = "SELECT * FROM exchangeRate WHERE changeDateTime = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                echangeRates.add(new EchangeRate(
                        resultSet.getInt("id"),
                        resultSet.getString("SourceCurrency"),
                        resultSet.getString("DestinationCurrency"),
                        resultSet.getDouble("Value"),
                        resultSet.getDate("ChangeDateTime")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return echangeRates;
    }

    public List<EchangeRate> getExchangeRatesForDate(List<EchangeRate> date) {
        return null;
    }
}
