package org.example.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private String name;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private Currency currency;
    private AccountType type;
    private LocalDateTime createdAt;

    public enum AccountType {
        SAVINGS, CHECKING, Bank, Cash, MobileMoney
    }

    public Account performTransaction(double amount, String description, Transaction.TransactionType transactionType) {
        if (transactionType == Transaction.TransactionType.DEBIT && amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for debit transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setLabel(description);
        transaction.setAmount(amount);
        transaction.setDate(new Date());
        transaction.setType(transactionType);

        if (transactionType == Transaction.TransactionType.CREDIT) {
            balance += amount;
        } else {
            balance -= amount;
        }
        transactions.add(transaction);

        return this;
    }

    public double getBalanceAtDateTime(String dateTime) {
        double balance = 0.0;
        for (Transaction transaction : transactions) {
            if (!transaction.getDate().after(java.sql.Timestamp.valueOf(dateTime))) {
                if (transaction.getType() == Transaction.TransactionType.CREDIT) {
                    balance += transaction.getAmount();
                } else {
                    balance -= transaction.getAmount();
                }
            }
        }
        return balance;
    }

    public List<Double> getBalanceHistory(String startDateTime, String endDateTime) {
        Date startDate = java.sql.Timestamp.valueOf(startDateTime);
        Date endDate = java.sql.Timestamp.valueOf(endDateTime);

        return transactions.stream()
                .filter(transaction -> {
                    Date transactionDate = transaction.getDate();
                    return !transactionDate.before(startDate) && !transactionDate.after(endDate);
                })
                .map(transaction -> getBalanceAtDateTime(transaction.getDate().toString()))
                .collect(Collectors.toList());
    }

    public void transferMoney(Account recipientAccount, double amount) {
        if (this.equals(recipientAccount)) {
            throw new IllegalArgumentException("Unable to transfer money to the same account.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance to effect transfer.");
        }

        this.balance -= amount;
        recipientAccount.balance += amount;

        this.performTransaction(amount, "Transferring to " + recipientAccount.getName(), Transaction.TransactionType.DEBIT);
        recipientAccount.performTransaction(amount, "Transferring from " + this.getName(), Transaction.TransactionType.CREDIT);
    }

    public void transferMoneyWithHistory(Account recipientAccount, double amount) {
        transferMoney(recipientAccount, amount);

        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setDebitTransactionId(this.transactions.get(this.transactions.size() - 1).getId());
        transferHistory.setCreditTransactionId(recipientAccount.transactions.get(recipientAccount.transactions.size() - 1).getId());
        transferHistory.setTransferDate(new Date());
    }
}
