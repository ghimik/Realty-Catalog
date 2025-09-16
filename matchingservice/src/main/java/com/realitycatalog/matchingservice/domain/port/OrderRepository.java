package com.realitycatalog.matchingservice.domain.port;

import com.realitycatalog.matchingservice.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository {
    Mono<Order> save(Order order);
    Mono<Order> findById(Long id);
    Flux<Order> findAll();
    Mono<Void> deleteById(Long id);
    Flux<Order> findOpenOrdersByTypeAndPriceRange(String type, Long priceMin, Long priceMax);

    Flux<Order> findOpenExchangeOrders(); // все открытые EXCHANGE ордера
    Flux<Order> findByClientId(Long clientId);

}
