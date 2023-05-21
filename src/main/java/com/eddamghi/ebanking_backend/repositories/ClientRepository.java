package com.eddamghi.ebanking_backend.repositories;

import com.eddamghi.ebanking_backend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
