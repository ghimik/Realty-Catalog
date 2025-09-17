package com.realitycatalog.matchingservice.infra.r2dbc.entity;


import com.realitycatalog.matchingservice.domain.model.TradeStatus;
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
@Table("trades")
public class TradeEntity {
    @Id
    private Long id;
    @Column("order_id")
    private Long orderId;
    @Column("counter_order_id")
    private Long counterOrderId;
    @Column("status")
    private TradeStatus status;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;
}
