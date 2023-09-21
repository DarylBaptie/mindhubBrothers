package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private boolean isActive;

    private AccountType accountType;

    Set<TransactionDTO> transactions = new HashSet<>();




    public AccountDTO(Account account) {

        this.id = account.getId();

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());

        this.isActive = account.getIsActive();

        this.accountType = account.getAccountType();
    }


    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    public double getBalance() {
        return balance;
    }


    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public AccountType getAccountType() {return accountType;}
}
