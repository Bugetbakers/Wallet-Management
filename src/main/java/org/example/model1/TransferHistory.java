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

}
