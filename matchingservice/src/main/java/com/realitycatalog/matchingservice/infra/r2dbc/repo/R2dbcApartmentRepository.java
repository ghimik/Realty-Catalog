package com.realitycatalog.matchingservice.infra.r2dbc.repo;


import com.realitycatalog.matchingservice.infra.r2dbc.entity.ApartmentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface R2dbcApartmentRepository extends R2dbcRepository<ApartmentEntity, Long> {
}