package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity

@RestController

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private String firstName;
    private String lastName;

    private String clientEmail;


    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    public Client() { }

    public Client(String first, String last, String email) {
        this.firstName = first;
        this.lastName = last;
        this.clientEmail = email;
    }

    public void setId() {
        this.id = id;
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
        return firstName + " " + lastName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

}
