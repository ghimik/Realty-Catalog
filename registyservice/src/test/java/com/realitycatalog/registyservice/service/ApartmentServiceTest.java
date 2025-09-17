package com.realitycatalog.registyservice.service;

import com.realitycatalog.registyservice.controller.dto.ApartmentDto;
import com.realitycatalog.registyservice.infra.postgre.model.Apartment;
import com.realitycatalog.registyservice.infra.postgre.repository.ApartmentRepository;
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
class ApartmentServiceTest {

    @Mock
    private ApartmentRepository repository;

    @InjectMocks
    private ApartmentService service;

    @Test
    void create_shouldSaveAndReturnApartment() {
        ApartmentDto dto = new ApartmentDto(1L, "Street 1", 2, 50.0, 3, 5000000L);
        Apartment saved = new Apartment();
        saved.setId(10L);
        saved.setOwnerId(dto.getOwnerId());

        when(repository.save(any(Apartment.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(dto))
                .expectNext(saved)
                .verifyComplete();

        verify(repository, times(1)).save(any(Apartment.class));
    }

    @Test
    void getAll_shouldReturnAll() {
        Apartment a1 = new Apartment(); a1.setId(1L);
        Apartment a2 = new Apartment(); a2.setId(2L);

        when(repository.findAll()).thenReturn(Flux.just(a1, a2));

        StepVerifier.create(service.getAll())
                .expectNext(a1, a2)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnApartment() {
        Apartment a = new Apartment(); a.setId(1L);

        when(repository.findById(1L)).thenReturn(Mono.just(a));

        StepVerifier.create(service.getById(1L))
                .expectNext(a)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    void update_shouldFindModifyAndSave() {
        ApartmentDto dto = new ApartmentDto(2L, "Updated", 3, 60.0, 5, 7000000L);
        Apartment existing = new Apartment(); existing.setId(1L);

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
