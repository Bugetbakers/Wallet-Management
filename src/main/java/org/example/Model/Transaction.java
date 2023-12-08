package org.example.Model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int id;
    private String label;
    private double amount;
    private LocalDateTime date;
    private TransactionType type;

    public enum TransactionType {
        CREDIT, DEBIT
    }
}
