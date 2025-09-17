package com.realitycatalog.matchingservice.application.config;

import com.realitycatalog.matchingservice.infra.adapter.ApartmentRepositoryAdapter;
import com.realitycatalog.matchingservice.infra.adapter.OrderRepositoryAdapter;
import com.realitycatalog.matchingservice.infra.adapter.TradeRepositoryAdapter;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcApartmentRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcOrderRepository;
import com.realitycatalog.matchingservice.infra.r2dbc.repo.R2dbcTradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfig {


    public @Bean ApartmentRepositoryAdapter apartmentRepositoryAdapter(@Autowired R2dbcApartmentRepository repository) {
        return new ApartmentRepositoryAdapter(repository);
    }

    public @Bean OrderRepositoryAdapter orderRepositoryAdapter(@Autowired R2dbcOrderRepository repository) {
        return new OrderRepositoryAdapter(repository);
    }

    public @Bean TradeRepositoryAdapter tradeRepositoryAdapter(@Autowired R2dbcTradeRepository repository) {
        return new TradeRepositoryAdapter(repository);
    }
}
