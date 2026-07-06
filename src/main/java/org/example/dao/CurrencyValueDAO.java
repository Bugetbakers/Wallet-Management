package org.example.dao;

import java.sql.Connection;

public class CurrencyValueDAO {
    private Connection connection;
    public CurrencyValueDAO(Connection connection) {
        this.connection = connection;
    }
}
