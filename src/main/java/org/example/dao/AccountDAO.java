package org.example.dao;

import org.example.model.Account;
import java.lang.reflect.Field;
import java.sql.*;

public class AccountDAO extends CrudOperationReflect<Account>{
    public AccountDAO(Connection connection){
        super(connection);
    }

    @Override
    protected String getTableName(){
        return "account";
    }

    @Override
    protected Account mapResultSetToObject(ResultSet resultSet) throws SQLException, IllegalAccessException{
        Account account= new Account();
        Field[] fields= account.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            field.set(account, resultSet.getObject(field.getName()));
        }
        return account;
    }

    @Override
    protected String getInsertColumns() {
        return "(id, name, id_currency, code, balance, balance_date, type)";
    }

    @Override
    protected String getInsertValues(){
        return "(?, ?, ?, ?, ?)";
    }

    @Override
    protected Account setPreparedStatementParameters(PreparedStatement preparedStatement, Account object) throws SQLException{
        preparedStatement.setInt(1, object.getId());

        return object;
    }
}
