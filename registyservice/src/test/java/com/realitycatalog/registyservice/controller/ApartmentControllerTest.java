package com.realitycatalog.registyservice.controller;

import com.realitycatalog.registyservice.controller.dto.ApartmentDto;
import com.realitycatalog.registyservice.infra.postgre.model.Apartment;
import com.realitycatalog.registyservice.service.ApartmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ApartmentController.class)
@Import(ApartmentControllerTest.TestConfig.class)
class ApartmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApartmentService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        ApartmentService apartmentService() {
            return Mockito.mock(ApartmentService.class);
        }
    }

    @Test
    void create_shouldReturnApartment() {
        ApartmentDto dto = new ApartmentDto(1L, "Street 1", 2, 45.5, 3, 5000000L);
        Apartment saved = new Apartment();
        saved.setId(10L);
        saved.setOwnerId(dto.getOwnerId());

        when(service.create(any(ApartmentDto.class))).thenReturn(Mono.just(saved));

        webTestClient.post().uri("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(10);
    }

    @Test
    void getAll_shouldReturnFluxOfApartments() {
        Apartment a1 = new Apartment(); a1.setId(1L);
        Apartment a2 = new Apartment(); a2.setId(2L);

        when(service.getAll()).thenReturn(Flux.just(a1, a2));

        webTestClient.get().uri("/apartments")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2);
    }

    @Test
    void getById_shouldReturnApartment() {
        Apartment a = new Apartment(); a.setId(1L);

        when(service.getById(1L)).thenReturn(Mono.just(a));

        webTestClient.get().uri("/apartments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void update_shouldReturnUpdatedApartment() {
        ApartmentDto dto = new ApartmentDto(2L, "Updated", 3, 55.0, 5, 7000000L);
        Apartment updated = new Apartment(); updated.setId(1L);

        when(service.update(eq(1L), any(ApartmentDto.class))).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/apartments/1")
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

        webTestClient.delete().uri("/apartments/1")
                .exchange()
                .expectStatus().isOk();
    }
}
