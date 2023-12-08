package org.example.Model;

import lombok.*;

import java.time.LocalDateTime;
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

    public enum TransactionType {
        CREDIT, type, DEBIT
    }
}
