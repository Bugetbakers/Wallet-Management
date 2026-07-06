package org.example.dao;

import org.example.model.ExchangeRate;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO {
    private Connection connection;

    public ExchangeRateDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ExchangeRate> getExchangeRatesForDate(LocalDateTime date) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "SELECT * FROM exchangeRate WHERE change_date_time = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(new ExchangeRate(
                        resultSet.getInt("id"),
                        resultSet.getString("source_currency"),
                        resultSet.getString("destination_currency"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("change_date_time")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }
}
