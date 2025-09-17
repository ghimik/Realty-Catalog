package com.realitycatalog.registyservice.service;


import com.realitycatalog.registyservice.controller.dto.ClientDto;
import com.realitycatalog.registyservice.infra.postgre.model.Client;
import com.realitycatalog.registyservice.infra.postgre.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;

    public Mono<Client> create(ClientDto dto) {
        Client c = new Client();
        c.setName(dto.getName());
        c.setPhone(dto.getPhone());
        c.setEmail(dto.getEmail());
        return repository.save(c);
    }

    public Flux<Client> getAll() {
        return repository.findAll();
    }

    public Mono<Client> getById(Long id) {
        return repository.findById(id);
    }

    public Mono<Client> update(Long id, ClientDto dto) {
        return repository.findById(id)
                .flatMap(c -> {
                    c.setName(dto.getName());
                    c.setPhone(dto.getPhone());
                    c.setEmail(dto.getEmail());
                    return repository.save(c);
                });
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
