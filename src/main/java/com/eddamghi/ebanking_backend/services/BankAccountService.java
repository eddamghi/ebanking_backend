package com.eddamghi.ebanking_backend.services;

import com.eddamghi.ebanking_backend.dtos.ClientDTO;
import com.eddamghi.ebanking_backend.dtos.CurrentBankAccountDTO;
import com.eddamghi.ebanking_backend.dtos.SavingBankAccountDTO;
import com.eddamghi.ebanking_backend.entities.BankAccount;
import com.eddamghi.ebanking_backend.entities.Client;
import com.eddamghi.ebanking_backend.entities.CurrentAccount;
import com.eddamghi.ebanking_backend.entities.SavingAccount;
import com.eddamghi.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.ClientNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {
    public ClientDTO saveClient(ClientDTO clientDTO);
    ClientDTO updateClient(ClientDTO clientDTO);
    void deleteClient(Long clientId);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long clientId) throws ClientNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long clientId) throws ClientNotFoundException;
    List<ClientDTO> getClients();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;
    List<BankAccount> getBankAccounts();
    ClientDTO getClient(Long id) throws ClientNotFoundException;
}
