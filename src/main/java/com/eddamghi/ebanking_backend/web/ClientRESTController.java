package com.eddamghi.ebanking_backend.web;

import com.eddamghi.ebanking_backend.dtos.ClientDTO;
import com.eddamghi.ebanking_backend.exceptions.ClientNotFoundException;
import com.eddamghi.ebanking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j

public class ClientRESTController {
    private BankAccountService bankAccountService;
    @GetMapping("/clients")
    public List<ClientDTO> clients() {
        return bankAccountService.getClients();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable(name = "id") Long clientId) throws ClientNotFoundException {
        return bankAccountService.getClient(clientId);
    }
    @PostMapping("/clients")
    public ClientDTO saveClient(@RequestBody ClientDTO clientDTO){
        return bankAccountService.saveClient(clientDTO);
    }
    @PutMapping("clients/{clientId}")
    public ClientDTO updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO){
        clientDTO.setId(clientId);
        return bankAccountService.updateClient(clientDTO);
    }
    @DeleteMapping("clients/{id}")
    public void deleteClient(@PathVariable Long id){
        bankAccountService.deleteClient(id);
    }
}
