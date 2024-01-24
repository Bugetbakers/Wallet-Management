package org.example.dao;

import org.example.model.Currency;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDAO extends CrudOperationReflect<Currency>{
    public CurrencyDAO(Connection connection){
        super(connection);
    }

    @Override
    protected String getTableName() {
        return "currency";
    }

    @Override
    protected String getInsertColumns() {
        return ("id, code, name");
    }

    @Override
    protected String getInsertValues() {
        return "(?, ?, ?)";
    }

    @Override
    protected Currency mapResultSetToObject(ResultSet resultSet) throws SQLException, IllegalAccessException {
        Currency currency= new Currency();
        Field[] fields= currency.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            field.set(currency, resultSet.getObject(field.getName()));
        }
        return currency;
    }

    @Override
    protected Currency setPreparedStatementParameters(PreparedStatement preparedStatement, Currency object) throws SQLException {
        preparedStatement.setInt(1, object.getId());

        return object;
    }
}