package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransferHistory {
    private int id;
    private int debitTransactionId;
    private int creditTransactionId;
    private double amount;
    private Date transferDate;
    private int transactionDate;

    public TransferHistory(int id, int debitTransactionId, int creditTransactionId, double amount, java.sql.Date date) {
    }

    public int getTransactionDate() {
        return transactionDate;
    }
}
