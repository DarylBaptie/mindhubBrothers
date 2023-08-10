package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@RestController

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;

    private TransactionType type;

    private LocalDateTime date;
    private double amount;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;
    public Transaction() {}

    public Transaction(TransactionType type, LocalDateTime date, double amount, String description) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public void setId(long id) {this.id = id;}

    public long getId() {
        return this.id;
    }

    public void setType(TransactionType type) {this.type = type;}

    public TransactionType getType() {return this.type;}

    public void setDate(LocalDateTime date) {this.date = date;}

    public LocalDateTime getDate() {return this.date;}

    public void setAmount(double amount) {this.amount = amount;}

    public double getAmount() {return this.amount;}

    public void setDescription(String description) {this.description = description;}

    public String getDescription() {return this.description;}

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

public String toString() {return id + " " + type + " " + date + " " + amount + " " + description;}


}
