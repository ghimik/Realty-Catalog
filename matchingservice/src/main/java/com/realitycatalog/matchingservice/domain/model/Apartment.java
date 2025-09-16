package com.realitycatalog.matchingservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    private Long id;
    private Long ownerId;
    private String address;
    private Integer rooms;
    private Double area;
    private Integer floor;
    private Long price;
}