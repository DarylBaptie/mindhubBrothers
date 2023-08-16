package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;

    private String clientEmail;

    private Set<ClientLoanDTO> loans;

    private Set<AccountDTO> accounts;

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.clientEmail = client.getClientEmail();

        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());

        this.loans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toSet());

    }

    public long getId() {
        return id;
    }

    public String getClientEmail() {
        return clientEmail;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getClientloans() {
        return loans;
    }
}