package com.realitycatalog.registyservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@ComponentScan(basePackages = "com.realitycatalog.registyservice.infra.postgre.repository")
@EnableR2dbcRepositories
public class R2DBCConfig {
}
