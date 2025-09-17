package com.realitycatalog.registyservice.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {
    private String name;
    private String phone;
    private String email;
}