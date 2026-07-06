package org.example.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.model.TransferHistory;

public class TransferHistoryDAO {
    private Connection connection;

    public TransferHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    public List<TransferHistory> getTransfersBeforeDate(int accountId, LocalDateTime date) {
        List<TransferHistory> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer_history WHERE debit_account_id = ? AND transfer_date < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                transfers.add(new TransferHistory(
                        resultSet.getInt("id"),
                        resultSet.getInt("debit_transaction_id"),
                        resultSet.getInt("credit_transaction_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("transfer_date")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transfers;
    }
}
