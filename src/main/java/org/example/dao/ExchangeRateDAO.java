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
        String sql = "SELECT * FROM ExchangeRate WHERE ChangeDateTime = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(date.toLocalDate()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ExchangeRate rate = new ExchangeRate();
                    rate.setId(resultSet.getInt("id"));
                    rate.setSourceCurrency(resultSet.getString("SourceCurrency"));
                    rate.setDestinationCurrency(resultSet.getString("DestinationCurrency"));
                    rate.setValue(resultSet.getDouble("Value"));
                    rate.setChangeDateTime(resultSet.getDate("ChangeDateTime"));
                    exchangeRates.add(rate);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }
}
