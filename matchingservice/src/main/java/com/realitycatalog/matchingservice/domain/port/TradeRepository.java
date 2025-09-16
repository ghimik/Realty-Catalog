package com.realitycatalog.matchingservice.domain.port;

import com.realitycatalog.matchingservice.domain.model.Trade;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TradeRepository {
    Mono<Trade> save(Trade trade);
    Mono<Trade> findById(Long id);
    Flux<Trade> findAll();
    Flux<Trade> findByClients(Long clientId, Long counterClientId);
}
