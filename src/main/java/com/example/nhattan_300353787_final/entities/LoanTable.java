package com.example.nhattan_300353787_final.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loantable")
public class LoanTable {
    @Id
    @Size(max = 8)
    private String clientno;

    @Size(max = 20)
    private String clientname;

    private double loanamount;

    private int years;

    @Size(max = 20)
    private String loantype;
}
