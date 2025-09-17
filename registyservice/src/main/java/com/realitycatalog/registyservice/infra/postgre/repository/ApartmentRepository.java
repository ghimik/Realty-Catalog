package com.realitycatalog.registyservice.infra.postgre.repository;


import com.realitycatalog.registyservice.infra.postgre.model.Apartment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ApartmentRepository extends ReactiveCrudRepository<Apartment, Long> {
    Flux<Apartment> findByOwnerId(Long ownerId);

}
