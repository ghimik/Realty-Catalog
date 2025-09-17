package com.realitycatalog.matchingservice.infra.r2dbc.repo;


import com.realitycatalog.matchingservice.infra.r2dbc.entity.TradeEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface R2dbcTradeRepository extends R2dbcRepository<TradeEntity, Long> {

    @Query("""
    SELECT t.* FROM trades t
    JOIN orders o1 ON t.order_id = o1.id
    JOIN orders o2 ON t.counter_order_id = o2.id
    WHERE (:clientId IS NULL OR o1.client_id = :clientId)
      AND (:counterClientId IS NULL OR o2.client_id = :counterClientId)
""")
    Flux<TradeEntity> findByClients(Long clientId, Long counterClientId);

}
