package org.example.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int id;
    private String label;
    private double amount;
    private Date date;
    private TransactionType type;
    private int category;

    public enum TransactionType {
        CREDIT, DEBIT
    }
}
