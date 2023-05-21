package com.eddamghi.ebanking_backend;

import com.eddamghi.ebanking_backend.dtos.ClientDTO;
import com.eddamghi.ebanking_backend.entities.*;
import com.eddamghi.ebanking_backend.enums.AccountStatus;
import com.eddamghi.ebanking_backend.enums.OperationType;
import com.eddamghi.ebanking_backend.entities.BankAccount;
import com.eddamghi.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.ClientNotFoundException;
import com.eddamghi.ebanking_backend.exceptions.InsufficientBalanceException;
import com.eddamghi.ebanking_backend.repositories.AccountOperationRepository;
import com.eddamghi.ebanking_backend.repositories.BankAccountRepository;
import com.eddamghi.ebanking_backend.repositories.ClientRepository;
import com.eddamghi.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
            CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("John Doe", "Jane Doe", "John Smith", "Jane Smith").forEach(name->{
                ClientDTO clientDTO = new ClientDTO();
                clientDTO.setName(name);
                clientDTO.setEmail(name.toLowerCase().replace(" ",".")+"@gmail.com");
                bankAccountService.saveClient(clientDTO);
             });
            bankAccountService.getClients().forEach(client -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000+1000,9000,client.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*9000+1000,0.05,client.getId());
                    List<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
                    for(BankAccount bankAccount : bankAccounts){
                        for(int i = 0; i < 10; i++)
                            bankAccountService.credit(bankAccount.getId(),Math.random() * 10000 + 12000, "Credit operation");
                            bankAccountService.debit(bankAccount.getId(),Math.random() * 10000 + 12000, "Debit operation");
                    }
                } catch (ClientNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | InsufficientBalanceException e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }
//    @Bean
    CommandLineRunner start(ClientRepository clientRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("John Doe", "Jane Doe", "John Smith", "Jane Smith").forEach(name -> {
                clientRepository.save(new Client(null, name, name.toLowerCase().replace(" ", ".") + "@gmail.com", null));
            });
            clientRepository.findAll().forEach(client -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 9000 + 1000);
                currentAccount.setCreatedAt(new java.util.Date());
                currentAccount.setOverdraft(9000.00);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setClient(client);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 9000 + 1000);
                savingAccount.setCreatedAt(new java.util.Date());
                savingAccount.setInterestRate(0.05);
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setClient(client);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random() * 9000 + 1000);
                    accountOperation.setOperationDate(new java.util.Date());
                    accountOperation.setBankAccount(bankAccount);
                    accountOperation.setOperationType(Math.random() > 0.5? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperationRepository.save(accountOperation);
                }
                    }
            );

        };

    }

}
