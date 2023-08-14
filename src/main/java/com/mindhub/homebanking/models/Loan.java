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
@RestController

public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")


    private long id;

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans;

    private String name;

    private double maxAmount;

    @ElementCollection
    @Column(name="Payment")
    private List<Integer> payments = new ArrayList<>();


    public Loan() {}

public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
}

public void setId(long id) {this.id = id;}

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

    @JsonIgnore
    public List<Loan> getLoans() {
        return clientLoans.stream().map(sub -> sub.getLoan()).collect(toList());
    }
}
