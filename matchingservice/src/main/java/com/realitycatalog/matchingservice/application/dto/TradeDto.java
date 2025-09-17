package com.realitycatalog.matchingservice.application.dto;

import com.realitycatalog.matchingservice.domain.model.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDto {
    private Long id;
    private Long orderId;
    private Long counterOrderId;
    private TradeStatus status;

    private Long initiatorClientId; // из orderId
    private Long counterClientId;   // из counterOrderId
}
