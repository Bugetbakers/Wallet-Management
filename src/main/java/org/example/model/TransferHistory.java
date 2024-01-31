package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
    @Getter
    private int transactionDate;


    public TransferHistory(int id, int debitTransactionId, int creditTransactionId, double amount, java.sql.Date transferDate) {
        this.id= id;
        this.debitTransactionId= debitTransactionId;
        this.creditTransactionId=creditTransactionId;
        this.amount=amount;
        this.transferDate= transferDate;
    }
}
