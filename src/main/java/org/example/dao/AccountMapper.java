package org.example.dao;

import org.example.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccontMapper extends reflectMapper<Account>{
    public List<Account> mapResultSetToList(ResultSet resultSet) {
        List<Account> accounts = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Account account = map(resultSet, Account.class);
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }
}
