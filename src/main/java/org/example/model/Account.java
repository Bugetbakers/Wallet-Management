package org.example.model;

import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
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

    public Object getTransactions() {
        return null;
    }

    public enum AccountType {
        Bank, Cash, MobilMoney
    }

    public Account performTransaction(double amount, String description, Transaction.TransactionType transactionType) {
        if (transactionType == Transaction.TransactionType.DEBIT && amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for debit transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setLabel(description);
        transaction.setAmount(amount);
        transaction.setDate(new Date());

        if (transactionType == Transaction.TransactionType.CREDIT) {
            balance += amount;
        } else {
            balance -= amount;
        }
        transactions.add(transaction);

        return this;
    }

    public double getBalanceAtDateTime(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mma");
        try {
            Date targetDate = dateFormat.parse(dateTime);
            double balance = 0.0;
            for (Transaction transaction : transactions) {
                Date transactionDate = dateFormat.parse(String.valueOf(transaction.getDate()));

                if (!transactionDate.after(targetDate)) {
                    if (transaction.getType() == Transaction.TransactionType.CREDIT) {
                        balance += transaction.getAmount();
                    } else {
                        balance -= transaction.getAmount();
                    }
                }
            }
            return balance;
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
                        try {
                            Date transactionDate = dateFormat.parse(String.valueOf(transaction.getDate()));
                            return !transactionDate.before(startDate) && !transactionDate.after(endDate);
                        } catch (ParseException e) {
                            throw new IllegalArgumentException("Date invalid : " + e.getMessage());
                        }
                    })
                    .map(transaction -> getBalanceAtDateTime(String.valueOf(transaction.getDate())))
                    .collect(Collectors.toList());

        } catch (ParseException e) {
            throw new IllegalArgumentException("Date invalid : " + e.getMessage());
        }
    }

    public void transferMoney(Account recipientAccount, double amount) {
        if (this.equals(recipientAccount)) {
            throw new IllegalArgumentException("Unable to transfer money to the same account.");
        } if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance to effect transfer.");
        }

        double newSourceBalance = balance - amount;
        double newRecipientBalance = recipientAccount.balance + amount;

        this.balance = newSourceBalance;
        recipientAccount.balance = newRecipientBalance;

        this.performTransaction(-amount, "Transferring to " + recipientAccount.getName(), Transaction.TransactionType.DEBIT);
        recipientAccount.performTransaction(amount, "Transferring from " + this.getName(), Transaction.TransactionType.CREDIT);
    }

    public void transferMoneyWithHistory(Account recipientAccount, double amount) {
        if (this.equals(recipientAccount)) {
            throw new IllegalArgumentException("Unable to transfer money to the same account.");
        } if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance to effect transfer.");
        }

        double newSourceBalance = balance - amount;
        double newRecipientBalance = recipientAccount.balance + amount;

        this.balance = newSourceBalance;
        recipientAccount.balance = newRecipientBalance;

        this.performTransaction(-amount, "Transferring to " + recipientAccount.getName(), Transaction.TransactionType.DEBIT);
        recipientAccount.performTransaction(amount, "Transferring from " + this.getName(), Transaction.TransactionType.CREDIT);

        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setDebitTransactionId(this.transactions.get(this.transactions.size() - 1).getId());
        transferHistory.setCreditTransactionId(recipientAccount.transactions.get(recipientAccount.transactions.size() - 1).getId());
        transferHistory.setTransferDate(new Date());
    }


    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    LocalDateTime currentDateTime = getCurrentDateTime();
    public void met() {
        System.out.println("Date et heure actuelles : " + currentDateTime);
    }
}
