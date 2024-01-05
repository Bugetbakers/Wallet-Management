package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EchangeRate {
    private int id;
    private String SourceCurrency;
    private String DestinationCurrency;
    private double Value;
    private Date ChangeDateTime;

}
