package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private long id;
    private TransactionType type;

    private LocalDateTime date;
    private double amount;
    private String description;

    private double balance;

    private boolean isActive;



    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.balance = transaction.getBalance();
        this.isActive = transaction.getIsActive();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {return this.type;}

    public LocalDateTime getDate() {return this.date;}

    public double getAmount() {return this.amount;}

    public String getDescription() {return this.description;}

    public double getBalance() {
        return balance;
    }

    public boolean getIsActive() {
        return isActive;
    }

}
