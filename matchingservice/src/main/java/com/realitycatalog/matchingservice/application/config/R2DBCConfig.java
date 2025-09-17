package com.realitycatalog.matchingservice.application.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.realitycatalog.matchingservice.infra.r2dbc.repo")
public class R2DBCConfig{



}
