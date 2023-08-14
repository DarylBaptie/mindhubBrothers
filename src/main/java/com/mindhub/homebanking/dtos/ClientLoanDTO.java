package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private long id;

    private long loanID;

    private double amount;

    private int payment;

    private String loanName;


    public ClientLoanDTO() {}

public ClientLoanDTO(ClientLoan clientLoan) {
    this.id = clientLoan.getId();
    this.amount = clientLoan.getAmount();
    this.payment = clientLoan.getPayment();
    this.loanName = clientLoan.getLoan().getName();
    this.loanID = clientLoan.getLoan().getId();
}

    public long getId() {return id;}


    public double getAmount() {return amount;}


    public int getPayment() {return payment;}



    public String getLoanName() {return loanName;}


    public long getLoanId() {return loanID;}


}