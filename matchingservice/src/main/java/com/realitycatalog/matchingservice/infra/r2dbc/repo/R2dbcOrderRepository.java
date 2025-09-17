package com.realitycatalog.matchingservice.infra.r2dbc.repo;


import com.realitycatalog.matchingservice.infra.r2dbc.entity.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;

public interface R2dbcOrderRepository extends R2dbcRepository<OrderEntity, Long> {

    @Query("SELECT * FROM orders WHERE type = :type AND status = 'OPEN' AND price_min <= :priceMax AND price_max >= :priceMin")
    Flux<OrderEntity> findOpenOrdersByTypeAndPriceRange(String type, Long priceMin, Long priceMax);

    @Query("SELECT * FROM orders WHERE type = 'EXCHANGE' AND status = 'OPEN'")
    Flux<OrderEntity> findOpenExchangeOrders();

    @Query("SELECT * FROM orders WHERE (:clientId IS NULL OR client_id = :clientId)")
    Flux<OrderEntity> findByClientId(Long clientId);

}

