package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {


    private long id;

    private String name;

    private double amount;

    private int payments;

    private String accountNumber;


    public long getId() {return id;}

    public String getName() {return name;}


    public double getAmount() {return amount;}

    public int getPayments() {return payments;}


    public String getAccountNumber() {return accountNumber;}



}
