package com.realitycatalog.matchingservice.application.dto;

import com.realitycatalog.matchingservice.domain.model.OrderStatus;
import com.realitycatalog.matchingservice.domain.model.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long clientId;
    private Long apartmentId;
    private OrderType type;
    private OrderStatus status;
    private Long priceMin;
    private Long priceMax;
}
