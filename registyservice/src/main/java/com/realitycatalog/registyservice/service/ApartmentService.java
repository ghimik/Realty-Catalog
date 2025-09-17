package com.realitycatalog.registyservice.service;


import com.realitycatalog.registyservice.controller.dto.ApartmentDto;
import com.realitycatalog.registyservice.infra.postgre.model.Apartment;
import com.realitycatalog.registyservice.infra.postgre.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository repository;

    public Mono<Apartment> create(ApartmentDto dto) {
        Apartment a = new Apartment();
        return getMono(dto, a);
    }

    public Flux<Apartment> getAll() {
        return repository.findAll();
    }

    public Mono<Apartment> getById(Long id) {
        return repository.findById(id);
    }

    public Mono<Apartment> update(Long id, ApartmentDto dto) {
        return repository.findById(id)
                .flatMap(a -> getMono(dto, a));
    }

    private Mono<Apartment> getMono(ApartmentDto dto, Apartment a) {
        if (dto.getOwnerId() != null)
            a.setOwnerId(dto.getOwnerId());
        if (dto.getAddress() != null)
            a.setAddress(dto.getAddress());
        if (dto.getRooms() != null)
            a.setRooms(dto.getRooms());
        if (dto.getArea() != null)
            a.setArea(dto.getArea());
        if (dto.getFloor() != null)
            a.setFloor(dto.getFloor());
        if (dto.getPrice() != null)
            a.setPrice(dto.getPrice());
        return repository.save(a);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    public Flux<Apartment> getByOwnerId(Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}
