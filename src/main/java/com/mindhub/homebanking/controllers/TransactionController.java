package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController

public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());

    }

    @RequestMapping("/api/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }


    @Transactional
    @RequestMapping(path = "/api/clients/current/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
        Authentication authentication,
        @RequestParam String amount,
        @RequestParam String description,
        @RequestParam String accountNumberOrigin,
        @RequestParam String accountNumberDestination

) {

    Account originAccount = accountRepository.findByNumber(accountNumberOrigin);
    Account destinationAccount = accountRepository.findByNumber(accountNumberDestination);


        if(description.isBlank()) {
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }

        if(amount.isBlank()) {
            return new ResponseEntity<>("Amount is missing", HttpStatus.FORBIDDEN);
        }

        if(accountNumberOrigin.isBlank()) {
            return new ResponseEntity<>("Origin account number is missing", HttpStatus.FORBIDDEN);
        }

    if(accountNumberDestination.isBlank()) {
        return new ResponseEntity<>("Destination account number is missing", HttpStatus.FORBIDDEN);
    }

    if(Double.valueOf(amount) <= 0 ) {
        return new ResponseEntity<>("Amount is not valid", HttpStatus.FORBIDDEN);
    }

    if(accountNumberOrigin.equals(accountNumberDestination)) {
        return new ResponseEntity<>("Destination and Origin account numbers cannot be the same", HttpStatus.FORBIDDEN);
    }


    if(accountRepository.findByNumber(accountNumberOrigin) == null) {
        return new ResponseEntity<>("Provided origin account number does not exist", HttpStatus.FORBIDDEN);
    }

    if(accountRepository.findByNumber(accountNumberOrigin).getBalance() < Double.valueOf(amount)) {
        return new ResponseEntity<>("Insufficient funds for this transaction", HttpStatus.FORBIDDEN);
    }

    if(!clientRepository.findByEmail(authentication.getName()).getAccounts().contains(originAccount)) {
        return new ResponseEntity<>("Account does not belong to client.", HttpStatus.FORBIDDEN);
    }

    if(accountRepository.findByNumber(accountNumberDestination) == null) {
        return new ResponseEntity<>("Provided destination account number does not exist", HttpStatus.FORBIDDEN);
    }



    Transaction transactionDebit = new Transaction(DEBIT, LocalDateTime.now(), (Double.valueOf(amount) * -1), description);
    Transaction transactionCredit = new Transaction(CREDIT, LocalDateTime.now(), Double.valueOf(amount), description);

    originAccount.setBalance(originAccount.getBalance() - Double.valueOf(amount));
    destinationAccount.setBalance(destinationAccount.getBalance() + Double.valueOf(amount));

    originAccount.addTransaction(transactionDebit);
    destinationAccount.addTransaction(transactionCredit);
    transactionRepository.save(transactionDebit);
    transactionRepository.save(transactionCredit);

    return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);

}


}
