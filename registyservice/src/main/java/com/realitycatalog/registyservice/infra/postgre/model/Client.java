package com.realitycatalog.registyservice.infra.postgre.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("clients")
public class Client {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String email;
}
