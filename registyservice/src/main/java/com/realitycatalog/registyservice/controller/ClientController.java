package com.realitycatalog.registyservice.controller;


import com.realitycatalog.registyservice.controller.dto.ClientDto;
import com.realitycatalog.registyservice.infra.postgre.model.Client;
import com.realitycatalog.registyservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping
    public Mono<Client> create(@RequestBody ClientDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public Flux<Client> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Client> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<Client> update(@PathVariable Long id, @RequestBody ClientDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}