package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {

    private String name;

    private double maxAmount;

    private List<Integer> payments;

    private List<Double> interest;



    public LoanDTO(Loan loan) {
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interest = loan.getInterest();
    }

    public String getName() {return name;}

    public double getMaxAmount() {return maxAmount;}

    public List<Integer> getPayments() {return payments;}

    public List<Double> getInterest() {
        return interest;
    }

}
