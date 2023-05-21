package com.eddamghi.ebanking_backend.dtos;

import com.eddamghi.ebanking_backend.enums.AccountStatus;
import lombok.Data;


import java.util.Date;


@Data
public class SavingBankAccountDTO {
    private String id;
    private Double balance;
    private Date createdAt;
    private AccountStatus status;
    private ClientDTO clientDTO;
    private double interestRate;
}
