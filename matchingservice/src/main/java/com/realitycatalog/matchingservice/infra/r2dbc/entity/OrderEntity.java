package com.realitycatalog.matchingservice.infra.r2dbc.entity;


import com.realitycatalog.matchingservice.domain.model.OrderStatus;
import com.realitycatalog.matchingservice.domain.model.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class OrderEntity {
    @Id
    private Long id;
    @Column("client_id")
    private Long clientId;
    @Column("apartment_id")
    private Long apartmentId;
    private OrderType type;
    private OrderStatus status;
    @Column("price_min")
    private Long priceMin;
    @Column("price_max")
    private Long priceMax;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;
}
