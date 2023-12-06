package org.example.Model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int id;
    private long senderAccountId;
    private long receiverAccountId;
    private double amount;
    private String currency;
    private String description;
}
