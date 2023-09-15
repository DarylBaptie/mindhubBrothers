package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
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

    @Autowired
    private TransactionService transactionService;

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


    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/api/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountById(id);

        if(client.getAccounts().contains(account)) {
        return new ResponseEntity<>(accountService.findAccountDTOById(id), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>( "unauthorized access",HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/api/clients/current/accounts")


    public ResponseEntity<Object> createAccount(Authentication authentication
    )  {
        if(clientService.findClientByEmail(authentication.getName()).getAccounts().size() < 3) {
            Boolean isActive = true;
            Account newAccount = new Account(randomNumber(), LocalDateTime.now(), 0, isActive);
        clientService.findClientByEmail(authentication.getName()).addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("New account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }

    }


    @PatchMapping("/api/clients/current/accounts/close")
    public ResponseEntity<Object> disableCard(@RequestParam long id, Authentication authentication){
        Account account = accountService.findAccountById(id);
        Client client = clientService.findClientByEmail(authentication.getName());
        Boolean accountOfClient = client.getAccounts().contains(account);

        if(account == null) {
            return  new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!accountOfClient){
            return  new ResponseEntity<>("This account does not belong to this client", HttpStatus.FORBIDDEN);
        }
        if(account.getIsActive() == false){
            return new ResponseEntity<>("This card is already deactivated", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() != 0.0) {
            return new ResponseEntity<>("The account balance must be 0 for deletion", HttpStatus.FORBIDDEN);
        }

        account.getTransactions().forEach(transaction -> {transaction.setIsActive(false); transactionService.saveTransaction(transaction);});

        account.getTransactions().forEach(trans -> {trans.setIsActive(false); transactionService.saveTransaction(trans);});

        account.setIsActive(false);
        accountService.saveAccount(account);

        return  new ResponseEntity<>("The account has been closed successfully", HttpStatus.OK);

    }




    }

