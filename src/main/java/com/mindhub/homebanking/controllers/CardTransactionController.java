package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardTransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;


@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api")
public class CardTransactionController {

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;


    @Transactional
    @PostMapping("/card/transactions")
    public ResponseEntity<Object> createCardsTransaction(@RequestBody CardTransactionDTO cardTransactionDTO) {

        Card card =  cardService.findByCardNumber(cardTransactionDTO.getCardNumber());

        if(card == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }
        if(!card.getIsActive()){
            return new ResponseEntity<>("Card not active", HttpStatus.FORBIDDEN);
        }
        if(card.getThruDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()))){
            return new ResponseEntity<>("Card has expired", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != cardTransactionDTO.getCvv()){
            return new ResponseEntity<>("Invalid CVV", HttpStatus.FORBIDDEN);
        }

        Client client = card.getClient();
        Account account = client.getAccounts().stream().filter(account1 -> account1.getBalance() >= cardTransactionDTO.getAmount()).findFirst().orElse(null);

        if(account == null){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if(cardTransactionDTO.getAmount() <= 0){
            return new ResponseEntity<>("Please enter an amount greater than 0", HttpStatus.FORBIDDEN);
        }

        if(cardTransactionDTO.getDescription().isBlank()){
            return new ResponseEntity<>("Please enter a description", HttpStatus.FORBIDDEN);
        }

        if(cardTransactionDTO.getDescription().length() > 15){
            return new ResponseEntity<>("Description cannot be longer than 15 characters", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT, LocalDateTime.now(), cardTransactionDTO.getAmount() * -1, cardTransactionDTO.getDescription(), account.getBalance() - cardTransactionDTO.getAmount(), true);
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);
        account.setBalance(account.getBalance() - cardTransactionDTO.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }


}



