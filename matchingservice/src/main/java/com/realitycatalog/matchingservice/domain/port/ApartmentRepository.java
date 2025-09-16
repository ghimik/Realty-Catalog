package com.realitycatalog.matchingservice.domain.port;

import com.realitycatalog.matchingservice.domain.model.Apartment;
import reactor.core.publisher.Mono;

public interface ApartmentRepository {

    Mono<Apartment> findById(Long id);

    Mono<Apartment> update(Apartment apartment);
}
