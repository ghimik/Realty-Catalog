package com.realitycatalog.matchingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long clientId;
    private Long apartmentId; // null для BUY
    private OrderType type;
    private OrderStatus status;
    private Long priceMin;
    private Long priceMax;
    private Instant createdAt;
    private Instant updatedAt;
}
