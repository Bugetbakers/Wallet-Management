package org.example.dao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import org.example.model.TransferHistory;
import org.example.model.EchangeRate;

public class CurrencyValueDAO {
    private Connection connection;
    public CurrencyValueDAO(Connection connection) {
        this.connection = connection;
    }

    // instantier
    TransferHistoryDAO transferHistoryDAO;
    EchangeRateDAO echangeRateDAO;


    public double getCurrentBalance(int accountId, LocalDateTime date) {
        List<TransferHistory> transfers = transferHistoryDAO.getTransfersBeforeDate(accountId, date);
        double totalAmount = 0;
        double totalWeight = 0;

        List<EchangeRate> exchangeRates = echangeRateDAO.getExchangeRatesForDate(date);

        for (TransferHistory transfer : transfers) {
            double exchangeRate = getWeightedExchangeRate(transfer.getTransactionDate(), exchangeRates);

            double amountInAriary = transfer.getAmount() * exchangeRate;

            totalAmount += amountInAriary;
            totalWeight += exchangeRate;
        }

        double averageExchangeRate = totalWeight > 0 ? totalAmount / totalWeight : 0;

        return totalAmount / averageExchangeRate;
    }


}
