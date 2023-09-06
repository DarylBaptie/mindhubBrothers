package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static java.lang.Double.NaN;
import static java.util.stream.Collectors.toList;


@RestController



public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;



    @RequestMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoansDTO();
    }


    @Transactional
    @RequestMapping(value="/api/loans", method= RequestMethod.POST)

    public ResponseEntity addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountByNumber(loanApplicationDTO.getAccountNumber());
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());
        Loan loan = loanService.findLoanByName(loanApplicationDTO.getName());
        Stream clientLoanNames = client.getClientLoans().stream().filter(cloan -> cloan.getLoan().getName().equals(loanApplicationDTO.getName()));


        if(authentication == null) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getName().isBlank()) {
            return new ResponseEntity<>("Loan name is empty. Please review requested loan", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getAmount() == NaN) {
            return new ResponseEntity<>("Invalid amount. Please review requested amount", HttpStatus.FORBIDDEN);
        }


        if(loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getPayments() == NaN ) {
            return new ResponseEntity<>("Invalid payment plan. Please review requested payment plan.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAccountNumber().isBlank()) {
            return new ResponseEntity<>("Account number is empty. Please review account number", HttpStatus.FORBIDDEN);
        }

        if(loanService.findLoanByName(loanApplicationDTO.getName()) == null) {
            return new ResponseEntity<>("Requested loan does not exist. Please review loan type", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Invalid payment plan. Please review requested payment plan", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Requested amount exceeds maximum permitted. Please review requested amount", HttpStatus.FORBIDDEN);
        }

        if(accountService.findAccountByNumber(loanApplicationDTO.getAccountNumber()) == null) {
            return new ResponseEntity<>("Invalid account number. Please review account number", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account number provided does not belong to client. Please review account number", HttpStatus.FORBIDDEN);
        }


        if(clientLoanNames.count() > 0) {
            return new ResponseEntity<>("You already have this loan type", HttpStatus.FORBIDDEN);
        }

        else {

        Transaction transactionCredit = new Transaction(CREDIT, LocalDateTime.now(), loanApplicationDTO.getAmount(), loanApplicationDTO.getName() + ": " + "Loan approved");
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());

        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        account.addTransaction(transactionCredit);
        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(transactionCredit);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
        }

    }
}
