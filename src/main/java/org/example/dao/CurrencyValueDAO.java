package org.example.dao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import org.example.model.TransferHistory;

public class CurrencyValueDAO {
    private Connection connection;

    public CurrencyValueDAO(Connection connection) {
        this.connection = connection;
    }

    private TransferHistoryDAO transferHistoryDAO;
    private ExchangeRateDAO exchangeRateDAO;

    public double getCurrentBalance(int accountId, LocalDateTime date) {
        List<TransferHistory> transfers = transferHistoryDAO.getTransfersBeforeDate(accountId, date);
        double totalBalance = 0;

        for (TransferHistory transfer : transfers) {
            totalBalance += transfer.getAmount();
        }

        return totalBalance;
    }
}
