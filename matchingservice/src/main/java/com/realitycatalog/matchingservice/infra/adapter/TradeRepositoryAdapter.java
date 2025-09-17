package com.realitycatalog.matchingservice.infra.adapter;


import com.realitycatalog.matchingservice.domain.model.Trade;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.entity.TradeEntity;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcOrderRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class TradeRepositoryAdapter implements TradeRepository {

    private final R2dbcTradeRepository repository;


    private Trade toDomain(TradeEntity e) {
        return new Trade(
                e.getId(),
                e.getOrderId(),
                e.getCounterOrderId(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private TradeEntity toEntity(Trade t) {
        return new TradeEntity(
                t.getId(),
                t.getOrderId(),
                t.getCounterOrderId(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }


    @Override
    public Mono<Trade> save(Trade trade) {
        return repository.save(toEntity(trade)).map(this::toDomain);
    }

    @Override
    public Mono<Trade> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Flux<Trade> findAll() {
        return repository.findAll().map(this::toDomain);
    }

    public Flux<Trade> findByClients(Long clientId, Long counterClientId) {
        return repository.findByClients(clientId, counterClientId)
                .map(this::toDomain);
    }

}

