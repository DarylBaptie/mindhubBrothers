package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {


    private long id;

    private String name;

    private double amount;

    private int payments;

    private double installmentAmount;

    private String accountNumber;


    public double getInstallmentAmount() {return installmentAmount;}
    public long getId() {return id;}

    public String getName() {return name;}


    public double getAmount() {return amount;}

    public int getPayments() {return payments;}


    public String getAccountNumber() {return accountNumber;}



}
