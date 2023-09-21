package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Entity


public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")


    private long id;

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    private String name;

    private double maxAmount;

    @ElementCollection
    @Column(name="Payments")
    private List<Integer> payments;

    private double interest;

    public Loan() {}



    public Loan(String name, double maxAmount, List<Integer> payments, double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
}



public long getId() {return id;}

public void setName(String name) {this.name = name;}

public String getName() {return name;}

public void setMaxAmount(double maxAmount) {this.maxAmount = maxAmount;}

public double getMaxAmount() {return maxAmount;}

public void setPayments(List<Integer> payments) {
        this.payments = payments;
}

public List<Integer> getPayments() {return payments;}


    public String toString() {return id + " " + name + " " + maxAmount + " " + payments;}


    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream().map(sub -> sub.getClient()).collect(toList());


    }
}
