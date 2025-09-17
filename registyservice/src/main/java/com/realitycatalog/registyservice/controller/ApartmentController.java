package com.realitycatalog.registyservice.controller;


import com.realitycatalog.registyservice.controller.dto.ApartmentDto;
import com.realitycatalog.registyservice.infra.postgre.model.Apartment;
import com.realitycatalog.registyservice.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService service;

    @PostMapping
    public Mono<Apartment> create(@RequestBody ApartmentDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public Mono<Apartment> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<Apartment> update(@PathVariable Long id, @RequestBody ApartmentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping
    public Flux<Apartment> getAll(@RequestParam(required = false) Long ownerId) {
        if (ownerId != null) {
            return service.getByOwnerId(ownerId);
        }
        return service.getAll();
    }

}
