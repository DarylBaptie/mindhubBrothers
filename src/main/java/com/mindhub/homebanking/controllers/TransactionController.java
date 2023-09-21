package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController

public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/api/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactionsDTO();

    }

    @GetMapping("/api/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionDTO(id);
    }


    @Transactional
    @PostMapping("/api/clients/current/transactions")
    public ResponseEntity<Object> createTransaction(
        Authentication authentication,
        @RequestParam double amount,
        @RequestParam String description,
        @RequestParam String accountNumberOrigin,
        @RequestParam String accountNumberDestination,
        @RequestParam String accountDestinationType

        ) {

    Account originAccount = accountService.findAccountByNumber(accountNumberOrigin);
    Account destinationAccount = accountService.findAccountByNumber(accountNumberDestination);

        if(amount <= 0 ) {
            return new ResponseEntity<>("Amount is not valid", HttpStatus.FORBIDDEN);
        }


        if(description.isBlank()) {
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }


        if(accountNumberOrigin.isBlank()) {
            return new ResponseEntity<>("Origin account number is missing", HttpStatus.FORBIDDEN);
        }

    if(accountNumberDestination.isBlank()) {
        return new ResponseEntity<>("Destination account number is missing", HttpStatus.FORBIDDEN);
    }


    if(accountNumberOrigin.equals(accountNumberDestination)) {
        return new ResponseEntity<>("Destination and Origin account numbers cannot be the same", HttpStatus.FORBIDDEN);
    }


    if(accountService.findAccountByNumber(accountNumberOrigin) == null) {
        return new ResponseEntity<>("Provided origin account number does not exist", HttpStatus.FORBIDDEN);
    }

    if(originAccount.getBalance() < amount) {
        return new ResponseEntity<>("Insufficient funds for this transaction", HttpStatus.FORBIDDEN);
    }

    if(!clientService.findClientByEmail(authentication.getName()).getAccounts().contains(originAccount)) {
        return new ResponseEntity<>("Account does not belong to client.", HttpStatus.FORBIDDEN);
    }

    if(accountService.findAccountByNumber(accountNumberDestination) == null) {
        return new ResponseEntity<>("Provided destination account number does not exist", HttpStatus.FORBIDDEN);
    }

    if(Objects.equals(accountDestinationType, "personal") && !destinationAccount.getIsActive()) {
        return new ResponseEntity<>("This account has been closed", HttpStatus.FORBIDDEN);
    }

    boolean isActive = true;
    double balanceDebit = originAccount.getBalance() - amount;
    double balanceCredit = destinationAccount.getBalance() + amount;

    Transaction transactionDebit = new Transaction(DEBIT, LocalDateTime.now(), (amount * -1), description + " " + destinationAccount.getNumber(), balanceDebit, isActive);
    Transaction transactionCredit = new Transaction(CREDIT, LocalDateTime.now(), amount, description + " " + originAccount.getNumber(), balanceCredit, isActive);

    originAccount.setBalance(originAccount.getBalance() - amount);
    destinationAccount.setBalance(destinationAccount.getBalance() + amount);

    originAccount.addTransaction(transactionDebit);
    destinationAccount.addTransaction(transactionCredit);
    transactionService.saveTransaction(transactionDebit);
    transactionService.saveTransaction(transactionCredit);

    return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);

}


}
