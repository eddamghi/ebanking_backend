package com.eddamghi.ebanking_backend.services;

import com.eddamghi.ebanking_backend.dtos.ClientDTO;
import com.eddamghi.ebanking_backend.dtos.CurrentBankAccountDTO;
import com.eddamghi.ebanking_backend.dtos.SavingBankAccountDTO;
import com.eddamghi.ebanking_backend.entities.*;
import com.eddamghi.ebanking_backend.enums.OperationType;
import com.eddamghi.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.ClientNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.InsufficientBalanceException;
import com.eddamghi.ebanking_backend.mappers.BankAccountMapperImpl;
import com.eddamghi.ebanking_backend.repositories.AccountOperationRepository;
import com.eddamghi.ebanking_backend.repositories.BankAccountRepository;
import com.eddamghi.ebanking_backend.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountImpl implements BankAccountService{
    private BankAccountRepository bankAccountRepository;
    private ClientRepository clientRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl mapper;
    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        log.info("saving new client");
        Client client = mapper.toClient(clientDTO);
        Client savedClient = clientRepository.save(client);
        return mapper.toClientDTO(savedClient);
    }
    @Override
    public ClientDTO getClient(Long id) throws ClientNotFoundException {
        Client client = clientRepository.findById(id).orElseThrow(()-> new ClientNotFoundException("client not found"));
        return mapper.toClientDTO(client);
    }
    @Override
    public List<ClientDTO> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(client -> mapper.toClientDTO(client)).collect(Collectors.toList());
    }
    @Override
    public ClientDTO updateClient(ClientDTO clientDTO){
        log.info("updating new client");
        Client client = mapper.toClient(clientDTO);
        Client savedClient = clientRepository.save(client);
        return mapper.toClientDTO(savedClient);
    }
    @Override
    public void deleteClient(Long clientId){
        clientRepository.deleteById(clientId);
    }
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null) {
            throw new ClientNotFoundException("client not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverdraft(overdraft);
        currentAccount.setClient(client);
        return mapper.toCurrentBankAccountDTO(bankAccountRepository.save(currentAccount));
    }
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId).orElse(null);
        if(client == null) {
            throw new ClientNotFoundException("client not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setClient(client);
        return mapper.toSavingBankAccountDTO(bankAccountRepository.save(savingAccount));
    }
    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("bank account not found"));
    }
    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException{
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("insufficient balance");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }
    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }
    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException {
        debit(accountIdSource, amount, "transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "transfer from " + accountIdSource);
    }
    @Override
    public List<BankAccount> getBankAccounts() {
            return bankAccountRepository.findAll();
    }


}
