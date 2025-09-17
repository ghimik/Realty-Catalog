package com.realitycatalog.matchingservice.infra.adapter;

import com.realitycatalog.matchingservice.domain.model.Apartment;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.entity.ApartmentEntity;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcApartmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApartmentRepositoryAdapter implements ApartmentRepository {

    private final R2dbcApartmentRepository repository;

    @Override
    public Mono<Apartment> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Mono<Apartment> update(Apartment apartment) {
        return repository.save(
                new ApartmentEntity(
                        apartment.getId(),
                        apartment.getOwnerId(),
                        apartment.getAddress(),
                        apartment.getRooms(),
                        apartment.getArea(),
                        apartment.getFloor(),
                        apartment.getPrice()
                )
        ).map(this::toDomain);


    }

    private Apartment toDomain(ApartmentEntity e) {
        return new Apartment(e.getId(), e.getOwnerId(), e.getAddress(),
                e.getRooms(), e.getArea(), e.getFloor(), e.getPrice());
    }
}
