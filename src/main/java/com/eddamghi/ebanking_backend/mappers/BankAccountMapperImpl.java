package com.eddamghi.ebanking_backend.mappers;

import com.eddamghi.ebanking_backend.dtos.ClientDTO;
import com.eddamghi.ebanking_backend.dtos.CurrentBankAccountDTO;
import com.eddamghi.ebanking_backend.dtos.SavingBankAccountDTO;
import com.eddamghi.ebanking_backend.entities.Client;
import com.eddamghi.ebanking_backend.entities.CurrentAccount;
import com.eddamghi.ebanking_backend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public ClientDTO toClientDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        BeanUtils.copyProperties(client, clientDTO);
        return clientDTO;
    }
    public Client toClient(ClientDTO clientDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);
        return client;
    }
    public SavingBankAccountDTO toSavingBankAccountDTO(SavingAccount savingAccount) {
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
        savingBankAccountDTO.setClientDTO(toClientDTO(savingAccount.getClient()));
        return savingBankAccountDTO;
    }

    public SavingAccount toSavingBankAccount(SavingBankAccountDTO savingBankAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setClient(toClient(savingBankAccountDTO.getClientDTO()));
        return savingAccount;
    }

    public CurrentAccount toCurrentBankAccount(CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setClient(toClient(currentBankAccountDTO.getClientDTO()));
        return currentAccount;
    }
    public CurrentBankAccountDTO toCurrentBankAccountDTO(CurrentAccount currentAccount) {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setClientDTO(toClientDTO(currentAccount.getClient()));
        return currentBankAccountDTO;
    }
}
