package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;

@org.springframework.web.bind.annotation.RestController

public class AccountController {
    @Autowired
    private AccountRepository repository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return repository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());

    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {

        Account account = repository.findById(id).orElse(null);

        AccountDTO accountDTO = new AccountDTO(account);

        return accountDTO;
    }

}
