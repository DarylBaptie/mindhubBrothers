package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController

public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    private String randomNumber() {
        String random;
        do {
            int number = getRandomNumber(0, 99999999);
            random = "VIN-" + number;
        } while (accountRepository.findByNumber(random)!= null);
            return random;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());

    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication) {

            return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }


    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount(Authentication authentication
    )  {
        if(clientRepository.findByEmail(authentication.getName()).getAccounts().size() < 3) {
        Account newAccount = new Account(randomNumber(), LocalDateTime.now(), 0);
        clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("New account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }

    }

}

