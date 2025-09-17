package com.realitycatalog.registyservice.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApartmentDto {
    private Long ownerId;
    private String address;
    private Integer rooms;
    private Double area;
    private Integer floor;
    private Long price;
}
