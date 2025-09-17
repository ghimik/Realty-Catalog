package com.realitycatalog.registyservice.controller;

import com.realitycatalog.registyservice.controller.dto.ClientDto;
import com.realitycatalog.registyservice.infra.postgre.model.Client;
import com.realitycatalog.registyservice.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ClientController.class)
@Import(ClientControllerTest.ClientControllerTestConfiguration.class)
class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClientService service;

    @TestConfiguration
    static class ClientControllerTestConfiguration {
        public @Bean ClientService clientService() {
            return Mockito.mock(ClientService.class);
        }
    }

    @Test
    void create_shouldReturnClient() {
        ClientDto dto = new ClientDto("Иван", "89991112233", "ivan@mail.com");
        Client saved = new Client(); saved.setId(1L);

        when(service.create(any(ClientDto.class))).thenReturn(Mono.just(saved));

        webTestClient.post().uri("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getAll_shouldReturnFluxOfClients() {
        Client c1 = new Client(); c1.setId(1L);
        Client c2 = new Client(); c2.setId(2L);

        when(service.getAll()).thenReturn(Flux.just(c1, c2));

        webTestClient.get().uri("/clients")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2);
    }

    @Test
    void getById_shouldReturnClient() {
        Client c = new Client(); c.setId(1L);

        when(service.getById(1L)).thenReturn(Mono.just(c));

        webTestClient.get().uri("/clients/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void update_shouldReturnUpdatedClient() {
        ClientDto dto = new ClientDto("Мария", "89994445566", "maria@mail.com");
        Client updated = new Client(); updated.setId(1L);

        when(service.update(eq(1L), any(ClientDto.class))).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void delete_shouldReturnOk() {
        when(service.delete(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/clients/1")
                .exchange()
                .expectStatus().isOk();
    }
}
