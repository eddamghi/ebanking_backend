package com.eddamghi.ebanking_backend.web;

import com.eddamghi.ebanking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BankAccountRESTController {
    private BankAccountService bankAccountService;


}
