package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyValue {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private double value;
    private Date date;
}
