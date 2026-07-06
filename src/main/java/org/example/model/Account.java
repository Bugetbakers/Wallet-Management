package org.example.model;

import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Getter
    private List<Transaction> transactions = new ArrayList<>();
    private Currency currency;
    private AccountType type;

    public enum AccountType {
        Bank, Cash, MobileMoney
    }

    public Account performTransaction(double amount, String description, Transaction.TransactionType transactionType) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (transactionType == Transaction.TransactionType.DEBIT && amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for debit transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setId(0); // Will be set by database or remains 0 for local instances
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

    public double getBalanceAtDateTime(Date targetDate) {
        double currentBalance = 0.0;
        for (Transaction transaction : transactions) {
            Date transactionDate = transaction.getDate();
            if (transactionDate != null && !transactionDate.after(targetDate)) {
                if (transaction.getType() == Transaction.TransactionType.CREDIT) {
                    currentBalance += transaction.getAmount();
                } else if (transaction.getType() == Transaction.TransactionType.DEBIT) {
                    currentBalance -= transaction.getAmount();
                }
            }
        }
        return currentBalance;
    }

    public double getBalanceAtDateTime(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mma");
        try {
            Date targetDate = dateFormat.parse(dateTime);
            return getBalanceAtDateTime(targetDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Format date invalid : " + e.getMessage());
        }
    }

    public List<Double> getBalanceHistory(String startDateTime, String endDateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mma");

        try {
            Date startDate = dateFormat.parse(startDateTime);
            Date endDate = dateFormat.parse(endDateTime);

            return transactions.stream()
                    .filter(transaction -> {
                        Date transactionDate = transaction.getDate();
                        return transactionDate != null && !transactionDate.before(startDate) && !transactionDate.after(endDate);
                    })
                    .map(transaction -> getBalanceAtDateTime(transaction.getDate()))
                    .collect(Collectors.toList());

        } catch (ParseException e) {
            throw new IllegalArgumentException("Date invalid : " + e.getMessage());
        }
    }

    public void transferMoney(Account recipientAccount, double amount) {
        if (this.equals(recipientAccount)) {
            throw new IllegalArgumentException("Unable to transfer money to the same account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        
        // performTransaction will validate balance, update balance, and add transaction details
        this.performTransaction(amount, "Transferring to " + recipientAccount.getName(), Transaction.TransactionType.DEBIT);
        recipientAccount.performTransaction(amount, "Transferring from " + this.getName(), Transaction.TransactionType.CREDIT);
    }

    public void transferMoneyWithHistory(Account recipientAccount, double amount) {
        if (this.equals(recipientAccount)) {
            throw new IllegalArgumentException("Unable to transfer money to the same account.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        this.performTransaction(amount, "Transferring to " + recipientAccount.getName(), Transaction.TransactionType.DEBIT);
        recipientAccount.performTransaction(amount, "Transferring from " + this.getName(), Transaction.TransactionType.CREDIT);

        TransferHistory transferHistory = new TransferHistory();
        if (!this.transactions.isEmpty()) {
            transferHistory.setDebitTransactionId(this.transactions.get(this.transactions.size() - 1).getId());
        }
        if (!recipientAccount.transactions.isEmpty()) {
            transferHistory.setCreditTransactionId(recipientAccount.transactions.get(recipientAccount.transactions.size() - 1).getId());
        }
        transferHistory.setAmount(amount);
        transferHistory.setTransferDate(new Date());
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    LocalDateTime currentDateTime = getCurrentDateTime();
    public void printCurrentDateTime() {
        System.out.println("Date et heure actuelles : " + currentDateTime);
    }
}
