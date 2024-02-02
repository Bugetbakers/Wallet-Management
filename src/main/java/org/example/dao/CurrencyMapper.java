package org.example.dao;

import org.example.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyMapper extends reflectMapper<Currency>{
    public List<Currency> mapResultSetToList(ResultSet resultSet) {
        List<Currency> currencies = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Currency currency = map(resultSet, Currency.class);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }
}
