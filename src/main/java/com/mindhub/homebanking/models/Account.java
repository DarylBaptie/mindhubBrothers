package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity

@RestController


public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String number;
    private LocalDateTime creationDate;

    private double balance;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Account() { }

    public Account(String num, LocalDateTime createDate, double bal) {
        this.number = num;
        this.creationDate = createDate;
        this.balance = bal;
    }

    public void setId() {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String toString() {
        return number + " " + creationDate + " " + balance;
    }

}
