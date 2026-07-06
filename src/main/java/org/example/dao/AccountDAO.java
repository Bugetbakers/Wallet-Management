package org.example.dao;

import org.example.model.Account;
import org.example.model.Currency;
import java.sql.*;

public class AccountDAO extends CrudOperationReflect<Account> {
    public AccountDAO(Connection connection){
        super(connection);
    }

    @Override
    protected String getTableName(){
        return "account";
    }

    @Override
    protected Account mapResultSetToObject(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setName(resultSet.getString("name"));
        account.setBalance(resultSet.getDouble("balance"));

        int currencyId = resultSet.getInt("currency");
        if (!resultSet.wasNull()) {
            Currency currency = new Currency();
            currency.setId(currencyId);
            account.setCurrency(currency);
        }

        String typeStr = resultSet.getString("type");
        if (typeStr != null) {
            try {
                account.setType(Account.AccountType.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                // Try to handle case differences or fallback
            }
        }
        return account;
    }

    @Override
    protected String getInsertColumns() {
        return "(id, name, balance, currency, type)";
    }

    @Override
    protected String getInsertValues(){
        return "(?, ?, ?, ?, ?)";
    }

    @Override
    protected void setPreparedStatementParameters(PreparedStatement preparedStatement, Account object) throws SQLException {
        preparedStatement.setInt(1, object.getId());
        preparedStatement.setString(2, object.getName());
        preparedStatement.setDouble(3, object.getBalance());
        if (object.getCurrency() != null) {
            preparedStatement.setInt(4, object.getCurrency().getId());
        } else {
            preparedStatement.setNull(4, Types.INTEGER);
        }
        if (object.getType() != null) {
            preparedStatement.setString(5, object.getType().name());
        } else {
            preparedStatement.setNull(5, Types.VARCHAR);
        }
    }
}
