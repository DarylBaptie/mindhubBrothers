package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private long id;
    private TransactionType type;

    private LocalDateTime date;
    private double amount;
    private String description;

    public TransactionDTO() {};

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setType(TransactionType type) {this.type = type;}

    public TransactionType getType() {return this.type;}

    public void setDate(LocalDateTime date) {this.date = date;}

    public LocalDateTime getDate() {return this.date;}

    public void setAmount(double amount) {this.amount = amount;}

    public double getAmount() {return this.amount;}

    public void setDescription(String description) {this.description = description;}

    public String getDescription() {return this.description;}

}
