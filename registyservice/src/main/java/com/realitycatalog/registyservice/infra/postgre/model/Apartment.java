package com.realitycatalog.registyservice.infra.postgre.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("apartments")
public class Apartment {
    @Id
    private Long id;
    private Long ownerId;
    private String address;
    private Integer rooms;
    private Double area;
    private Integer floor;
    private Long price;
}
