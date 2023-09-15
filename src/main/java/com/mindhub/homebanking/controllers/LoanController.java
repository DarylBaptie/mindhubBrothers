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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


import static com.mindhub.homebanking.models.TransactionType.CREDIT;



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



    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoansDTO();
    }


    @Transactional
    @PostMapping("/api/loans")

    public ResponseEntity addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountByNumber(loanApplicationDTO.getAccountNumber());
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments(),loanApplicationDTO.getInstallmentAmount(), true);
        Loan loan = loanService.findLoanByName(loanApplicationDTO.getName());
        Stream clientLoanNames = client.getClientLoans().stream().filter(cloan -> cloan.getLoan().getName().equals(loanApplicationDTO.getName()) && cloan.isActive());

        if(authentication == null) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getName().isBlank()) {
            return new ResponseEntity<>("Loan name is empty. Please review requested loan", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Invalid amount. Please review requested amount", HttpStatus.FORBIDDEN);
        }


        if(loanApplicationDTO.getPayments() <= 0) {
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

        double newBalance = account.getBalance() + loanApplicationDTO.getAmount();

        Transaction transactionCredit = new Transaction(CREDIT, LocalDateTime.now(), loanApplicationDTO.getAmount(), loanApplicationDTO.getName() + ": " + "Loan approved", newBalance, true);
        account.setBalance(newBalance);

        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        account.addTransaction(transactionCredit);
        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(transactionCredit);

        return new ResponseEntity<>(account, HttpStatus.CREATED);
        }

    }

    @Transactional
    @PatchMapping("/api/clients/current/loans/loanPayment")
    public ResponseEntity addLoan(@RequestParam long loanId, @RequestParam long accountId, double paymentAmount,  Authentication authentication) {

        ClientLoan clientLoan = clientLoanService.findClientLoanById(loanId);
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findAccountById(accountId);

        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorised account", HttpStatus.UNAUTHORIZED);
        }

        if (!client.getClientLoans().contains(clientLoan)) {
            return new ResponseEntity<>("This loan does not belong to this client", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The selected account does not belong to this client", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() < paymentAmount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getAmount() == 0) {
            return new ResponseEntity<>("This loan is already paid off", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getAmount() < paymentAmount) {
            return new ResponseEntity<>("Payment amount exceeds loan balance", HttpStatus.FORBIDDEN);
        }

        if (clientLoan.getPayment() == 0) {
            return new ResponseEntity<>("All installments have been paid", HttpStatus.FORBIDDEN);
        }

        else {

            account.setBalance(account.getBalance() - paymentAmount);
            Transaction transaction = new Transaction(TransactionType.DEBIT, LocalDateTime.now(), paymentAmount, "Loan Installment - " + clientLoan.getLoan().getName(), account.getBalance(), true);
            clientLoan.setPayment(clientLoan.getPayment() - 1);
            clientLoan.setAmount((int) (clientLoan.getAmount() - paymentAmount));
            clientLoanService.saveClientLoan(clientLoan);
            account.addTransaction(transaction);
            transactionService.saveTransaction(transaction);

            if(clientLoan.getAmount() == 0) {
                clientLoan.setActive(false);
                clientLoanService.saveClientLoan(clientLoan);
            }

            return new ResponseEntity<>("payment successful", HttpStatus.CREATED);


        }

    }
}
