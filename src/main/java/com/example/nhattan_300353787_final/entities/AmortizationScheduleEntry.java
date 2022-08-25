package com.example.nhattan_300353787_final.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmortizationScheduleEntry {
    private int entryNo;
    private double remainingPrincipalAmount;
    private double interestPayment;
    private double principalPayment;
}
