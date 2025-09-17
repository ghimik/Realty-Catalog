package com.realitycatalog.registyservice.service;

import com.realitycatalog.registyservice.controller.dto.ClientDto;
import com.realitycatalog.registyservice.infra.postgre.model.Client;
import com.realitycatalog.registyservice.infra.postgre.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService service;

    @Test
    void create_shouldSaveAndReturnClient() {
        ClientDto dto = new ClientDto("John", "12345", "john@mail.com");
        Client saved = new Client(); saved.setId(1L);

        when(repository.save(any(Client.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(dto))
                .expectNext(saved)
                .verifyComplete();

        verify(repository).save(any(Client.class));
    }

    @Test
    void getAll_shouldReturnAllClients() {
        Client c1 = new Client(); c1.setId(1L);
        Client c2 = new Client(); c2.setId(2L);

        when(repository.findAll()).thenReturn(Flux.just(c1, c2));

        StepVerifier.create(service.getAll())
                .expectNext(c1, c2)
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void getById_shouldReturnClient() {
        Client c = new Client(); c.setId(1L);

        when(repository.findById(1L)).thenReturn(Mono.just(c));

        StepVerifier.create(service.getById(1L))
                .expectNext(c)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    void update_shouldFindModifyAndSave() {
        ClientDto dto = new ClientDto("Alice", "67890", "alice@mail.com");
        Client existing = new Client(); existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Mono.just(existing));
        when(repository.save(existing)).thenReturn(Mono.just(existing));

        StepVerifier.create(service.update(1L, dto))
                .expectNext(existing)
                .verifyComplete();

        verify(repository).findById(1L);
        verify(repository).save(existing);
    }

    @Test
    void delete_shouldCallRepository() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(1L))
                .verifyComplete();

        verify(repository).deleteById(1L);
    }
}
