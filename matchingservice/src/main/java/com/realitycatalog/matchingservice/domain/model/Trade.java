package com.realitycatalog.matchingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private Long id;
    private Long orderId;
    private Long counterOrderId;
    private TradeStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}