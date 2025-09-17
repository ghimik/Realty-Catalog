package com.realitycatalog.matchingservice.infra.r2dbc.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("apartments")
public class ApartmentEntity {
    @Id
    private Long id;
    @Column("owner_id")
    private Long ownerId;
    private String address;
    private Integer rooms;
    private Double area;
    private Integer floor;
    private Long price;
}
