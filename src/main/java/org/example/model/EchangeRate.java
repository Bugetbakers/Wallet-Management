package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class EchangeRate {
    private int id;
    private String sourceCurrency;
    private String destinationCurrency;
    private double value;
    private Date changeDateTime;
}
