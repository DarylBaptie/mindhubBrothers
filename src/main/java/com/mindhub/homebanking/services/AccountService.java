package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    void saveAccount(Account account);

    AccountDTO findAccountDTOById(long id);

    Account findAccountById(long id);

    Account findAccountByNumber(String number);


}
