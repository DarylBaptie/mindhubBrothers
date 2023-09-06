package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController

public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    private String randomNumber() {
        String random;
        do {
            int number = getRandomNumber(0, 99999999);
            random = "VIN-" + number;
        } while (accountService.findAccountByNumber(random)!= null);
            return random;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping("/api/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountById(id);

        if(client.getAccounts().contains(account)) {
        return new ResponseEntity<>(accountService.findAccountDTOById(id), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>( "unauthorized access",HttpStatus.FORBIDDEN);
        }
    }


    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> createAccount(Authentication authentication
    )  {
        if(clientService.findClientByEmail(authentication.getName()).getAccounts().size() < 3) {
        Account newAccount = new Account(randomNumber(), LocalDateTime.now(), 0);
        clientService.findClientByEmail(authentication.getName()).addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("New account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }

    }

}

