package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
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

    private int maxAmount;

    @ElementCollection
    private List<Integer> payments;


    public Loan() {}

public Loan(String name, int maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
}

public void setId(long id) {this.id = id;}

public long getId() {return id;}

public void setName(String name) {this.name = name;}

public String getName() {return name;}

public void setMaxAmount(int maxAmount) {this.maxAmount = maxAmount;}

public int getMaxAmount() {return maxAmount;}

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
