package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoansDTO();

    void saveLoan(Loan loan);

    Loan findLoanByName(String name);
}
