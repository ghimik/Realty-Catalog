package com.realitycatalog.matchingservice.application.service;

import com.realitycatalog.matchingservice.application.dto.OrderDto;
import com.realitycatalog.matchingservice.application.dto.TradeDto;
import com.realitycatalog.matchingservice.domain.model.Order;
import com.realitycatalog.matchingservice.domain.model.Trade;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import com.realitycatalog.matchingservice.domain.service.OrderDomainService;
import com.realitycatalog.matchingservice.domain.service.TradeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RequiredArgsConstructor
public class MatchingApplicationService {

    private final OrderDomainService orderDomainService;
    private final TradeDomainService tradeDomainService;
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;

    public Mono<OrderDto> createOrder(OrderDto dto) {
        Order order = new Order(
                dto.getId(),
                dto.getClientId(),
                dto.getApartmentId(),
                dto.getType(),
                dto.getStatus(),
                dto.getPriceMin(),
                dto.getPriceMax(),
                Instant.now(),
                Instant.now()
        );

        return orderDomainService.createOrder(order)
                .map(o -> new OrderDto(
                        o.getId(),
                        o.getClientId(),
                        o.getApartmentId(),
                        o.getType(),
                        o.getStatus(),
                        o.getPriceMin(),
                        o.getPriceMax()
                ));
    }

    public Mono<Void> deleteOrder(Long id) {
        return orderRepository.deleteById(id);

    }

    public Flux<OrderDto> matchOrder(Long orderId) {
        return orderDomainService.matchOrder(orderId)
                .map(o -> new OrderDto(
                        o.getId(),
                        o.getClientId(),
                        o.getApartmentId(),
                        o.getType(),
                        o.getStatus(),
                        o.getPriceMin(),
                        o.getPriceMax()
                ));
    }

    public Mono<TradeDto> acceptTrade(Long tradeId) {
        return tradeDomainService.acceptTrade(tradeId)
                .flatMap(this::fromDomain);
    }

    public Mono<TradeDto> rejectTrade(Long tradeId) {
        return tradeDomainService.rejectTrade(tradeId)
                .flatMap(this::fromDomain);
    }

    public Mono<TradeDto> createTrade(Long orderId, Long counterOrderId) {
        return Mono.zip(
                        orderRepository.findById(orderId),
                        orderRepository.findById(counterOrderId)
                ).flatMap(tuple -> tradeDomainService.createTrade(tuple.getT1(), tuple.getT2()))
                .flatMap(this::fromDomain);
    }


    public Flux<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .map(o -> new OrderDto(
                        o.getId(),
                        o.getClientId(),
                        o.getApartmentId(),
                        o.getType(),
                        o.getStatus(),
                        o.getPriceMin(),
                        o.getPriceMax()
                ));
    }

    public Mono<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(o -> new OrderDto(
                        o.getId(),
                        o.getClientId(),
                        o.getApartmentId(),
                        o.getType(),
                        o.getStatus(),
                        o.getPriceMin(),
                        o.getPriceMax()
                ));
    }

    public Flux<TradeDto> getAllTrades() {
        return tradeRepository.findAll()
                .flatMap(this::fromDomain);
    }

    public Mono<TradeDto> getTradeById(Long id) {
        return tradeRepository.findById(id)
                .flatMap(this::fromDomain);
    }


    public Flux<TradeDto> getTrades(Long clientId, Long counterClientId) {
        return tradeRepository.findByClients(clientId, counterClientId)
                .flatMap(this::fromDomain);
    }

    public Flux<OrderDto> getOrders(Long clientId) {
        return orderRepository.findByClientId(clientId)
                .map(o -> new OrderDto(
                        o.getId(), o.getClientId(), o.getApartmentId(),
                        o.getType(), o.getStatus(), o.getPriceMin(), o.getPriceMax()
                ));
    }

    private Mono<TradeDto> fromDomain(Trade trade) {
        return Mono.zip(orderRepository.findById(trade.getOrderId()),
                orderRepository.findById(trade.getCounterOrderId()))
                .map(t -> new TradeDto(
                        trade.getId(),
                        trade.getOrderId(),
                        trade.getCounterOrderId(),
                        trade.getStatus(),
                        t.getT1().getId(),
                        t.getT2().getId()));


    }




}
