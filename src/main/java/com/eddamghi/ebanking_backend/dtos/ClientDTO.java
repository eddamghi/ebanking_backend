package com.eddamghi.ebanking_backend.dtos;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String email;
}