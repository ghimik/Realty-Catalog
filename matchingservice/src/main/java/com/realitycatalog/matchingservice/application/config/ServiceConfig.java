package com.realitycatalog.matchingservice.application.config;

import com.realitycatalog.matchingservice.application.service.MatchingApplicationService;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import com.realitycatalog.matchingservice.domain.service.OrderDomainService;
import com.realitycatalog.matchingservice.domain.service.TradeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    public @Bean OrderDomainService orderDomainService(
            @Autowired OrderRepository orderRepository,
            @Autowired TradeRepository tradeRepository,
            @Autowired ApartmentRepository apartmentRepository
            ) {
        return new OrderDomainService(orderRepository, tradeRepository, apartmentRepository);
    }

    public @Bean TradeDomainService tradeDomainService(
            @Autowired OrderRepository orderRepository,
            @Autowired TradeRepository tradeRepository,
            @Autowired ApartmentRepository apartmentRepository
            ) {
        return new TradeDomainService(tradeRepository, orderRepository, apartmentRepository);
    }

    public @Bean MatchingApplicationService matchingApplicationService(
            @Autowired OrderDomainService orderDomainService,
            @Autowired TradeDomainService tradeDomainService,
            @Autowired OrderRepository orderRepository,
            @Autowired TradeRepository tradeRepository
    ) {
        return new MatchingApplicationService(orderDomainService, tradeDomainService, orderRepository, tradeRepository);
    }

}
