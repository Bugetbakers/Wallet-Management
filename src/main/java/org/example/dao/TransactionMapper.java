package org.example.dao;

import org.example.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionMapper extends reflectMapper<Transaction>{
    public List<Transaction> mapResultsetToList(ResultSet resultSet) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Transaction transaction = map(resultSet, Transaction.class);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
