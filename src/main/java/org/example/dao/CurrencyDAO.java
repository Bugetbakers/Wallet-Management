package org.example.dao;

import org.example.model.Currency;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDAO extends CrudOperationReflect<Currency> {
    public CurrencyDAO(Connection connection){
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "currency";
    }

    @Override
    protected String getInsertColumns() {
        return "(id, code, name)";
    }

    @Override
    protected String getInsertValues() {
        return "(?, ?, ?)";
    }

    @Override
    protected Currency mapResultSetToObject(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("name"));
        return currency;
    }

    @Override
    protected void setPreparedStatementParameters(PreparedStatement preparedStatement, Currency object) throws SQLException {
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getCode());
        preparedStatement.setString(3, object.getName());
    }
}