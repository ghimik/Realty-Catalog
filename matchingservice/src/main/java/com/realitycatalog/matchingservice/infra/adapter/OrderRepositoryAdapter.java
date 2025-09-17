package com.realitycatalog.matchingservice.infra.adapter;

import com.realitycatalog.matchingservice.domain.model.Order;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.entity.OrderEntity;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final R2dbcOrderRepository repository;

    private Order toDomain(OrderEntity e) {
        return new Order(
                e.getId(),
                e.getClientId(),
                e.getApartmentId(),
                e.getType(),
                e.getStatus(),
                e.getPriceMin(),
                e.getPriceMax(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private OrderEntity toEntity(Order o) {
        return new OrderEntity(
                o.getId(),
                o.getClientId(),
                o.getApartmentId(),
                o.getType(),
                o.getStatus(),
                o.getPriceMin(),
                o.getPriceMax(),
                o.getCreatedAt(),
                o.getUpdatedAt()
        );
    }


    @Override
    public Mono<Order> save(Order order) {
        return repository.save(toEntity(order)).map(this::toDomain);
    }

    @Override
    public Mono<Order> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Order> findAll() {
        return repository.findAll().map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Order> findOpenOrdersByTypeAndPriceRange(String type, Long priceMin, Long priceMax) {
        return repository.findOpenOrdersByTypeAndPriceRange(type, priceMin, priceMax).map(this::toDomain);
    }

    @Override
    public Flux<Order> findOpenExchangeOrders() {
        return repository.findOpenExchangeOrders().map(this::toDomain);
    }

    @Override
    public Flux<Order> findByClientId(Long clientId) {
        return repository.findByClientId(clientId).map(this::toDomain);
    }
}
