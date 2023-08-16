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


public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")



    private long id;
    private String firstName;
    private String lastName;

    private String clientEmail;


    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Client() { }

    public Client(String first, String last, String email) {
        this.firstName = first;
        this.lastName = last;
        this.clientEmail = email;
    }


    public long getId() {
        return id;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return id + " " + firstName + " " + lastName + " " + clientEmail;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }


    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }


    @JsonIgnore
    public List<Client> getLoans() {
        return clientLoans.stream().map(sub -> sub.getClient()).collect(toList());
    }


}
