package org.example.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private String name;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private String currency;

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public Account performTransaction(double amount, String description, TransactionType transactionType) {
        if (transactionType == TransactionType.DEBIT && amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for debit transaction");
        }

        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(id);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCurrency(currency);

        // Update balance based on transaction type
        if (transactionType == TransactionType.CREDIT) {
            balance += amount;
        } else {
            balance -= amount;
        }

        // Add the transaction to the transaction history
        transactions.add(transaction);

        // Return the updated account
        return this;
    }
}
