package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;

    private String clientEmail;

    Set<AccountDTO> accounts = new HashSet<>();

    public ClientDTO() {}
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.clientEmail = client.getClientEmail();

this.accounts = new HashSet<>();

for (Account account: client.getAccounts()) {
this.accounts.add(new AccountDTO(account));
}
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

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }


}