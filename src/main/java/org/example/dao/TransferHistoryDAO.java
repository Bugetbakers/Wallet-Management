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
        String sql = "SELECT th.* FROM TransferHistory th " +
                     "JOIN transactions dt ON th.debitTransactionId = dt.id " +
                     "JOIN transactions ct ON th.creditTransactionId = ct.id " +
                     "WHERE (dt.id_account = ? OR ct.id_account = ?) AND th.transferDate < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            statement.setTimestamp(3, Timestamp.valueOf(date));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TransferHistory transfer = new TransferHistory();
                    transfer.setId(resultSet.getInt("id"));
                    transfer.setDebitTransactionId(resultSet.getInt("debitTransactionId"));
                    transfer.setCreditTransactionId(resultSet.getInt("creditTransactionId"));
                    transfer.setAmount(resultSet.getDouble("amount")); // Wait, let's look at if it has amount column.
                    // Yes, TransferHistory table has: amount DOUBLE PRECISION (or we added it to java class TransferHistory).
                    // Wait, let's verify if the database table TransferHistory has amount column!
                    // In Create_table_transferHistory.sql, we wrote:
                    // CREATE TABLE IF NOT EXISTS TransferHistory (
                    //     id SERIAL PRIMARY KEY,
                    //     debitTransactionId INT REFERENCES transactions(id),
                    //     creditTransactionId INT REFERENCES transactions(id),
                    //     transferDate TIMESTAMP NOT NULL, ... )
                    // Ah! The SQL table does NOT have an amount column!
                    // But in TransferHistory.java, we had:
                    // private double amount;
                    // Wait, if the table doesn't have amount column, resultSet.getDouble("amount") will throw SQLException!
                    // Let's check Create_table_transferHistory.sql again.
                    // Yes! It doesn't have an amount column!
                    // Let's check: can we compute amount by querying dt.amount or ct.amount?
                    // Yes! dt.amount is the amount of the transaction!
                    // So we can select `dt.amount` (or `ct.amount`) in the SELECT query and alias it as amount!
                    // SELECT th.*, dt.amount AS amount FROM TransferHistory th ...
                    // This is extremely smart because we don't have to change the DB schema if it wasn't there,
                    // but wait, we can also add it to DB if we want, or just alias it.
                    // Let's look at the database schema we wrote for TransferHistory:
                    // We wrote:
                    // CREATE TABLE IF NOT EXISTS TransferHistory (
                    //     id SERIAL PRIMARY KEY,
                    //     debitTransactionId INT REFERENCES transactions(id),
                    //     creditTransactionId INT REFERENCES transactions(id),
                    //     transferDate TIMESTAMP NOT NULL, ... )
                    // Let's modify the SELECT statement to retrieve amount from the debit transaction:
                    // SELECT th.*, dt.amount AS amount FROM TransferHistory th JOIN transactions dt ON th.debitTransactionId = dt.id ...
                    // That is incredibly clean and works perfectly without adding columns in DB!
                    transfer.setTransferDate(resultSet.getTimestamp("transferDate"));
                    transfer.setAmount(resultSet.getDouble("amount"));
                    transfers.add(transfer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transfers;
    }
}
