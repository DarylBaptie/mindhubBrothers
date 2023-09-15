package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.web.bind.annotation.RestController;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity


public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;
    private double amount;

    private int payment;

    private double installmentAmount;

    private boolean isActive;

    public ClientLoan() {}




    public ClientLoan(double amount, int payment, double installmentAmount, boolean isActive) {
        this.amount = amount;
        this.payment = payment;
        this.installmentAmount = installmentAmount;
        this.isActive = isActive;
    }


    public long getId() {return id;}

    public void setAmount(int amount) {this.amount = amount;}

    public double getAmount() {return amount;}

    public void setPayment(int payment) {this.payment = payment;}

    public int getPayment() {return payment;}


    public void setClient(Client client) { this.client = client;}


    public Client getClient() {return client;}

    public void setLoan(Loan loan) {this.loan = loan;}


    public Loan getLoan() {return loan;}

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return id + " " + amount + " " + payment + " " + client + " " + loan;
    }



}
