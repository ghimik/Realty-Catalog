package com.realitycatalog.registyservice.infra.postgre.repository;


import com.realitycatalog.registyservice.infra.postgre.model.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {}
