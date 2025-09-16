package com.realitycatalog.matchingservice.domain.service;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
public class TradeDomainService {

    private final TradeRepository tradeRepository;

    private final OrderRepository orderRepository;

    private final ApartmentRepository apartmentRepository;

    public Mono<Trade> acceptTrade(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Trade not found" + tradeId)))
                .flatMap(trade -> {
                    trade.setStatus(TradeStatus.EXECUTED);
                    trade.setUpdatedAt(Instant.now());

                    return orderRepository.findById(trade.getOrderId())
                            .zipWith(orderRepository.findById(trade.getCounterOrderId()))
                            .flatMap(tuple -> {
                                Order order = tuple.getT1();
                                Order counterOrder = tuple.getT2();
                                order.setStatus(OrderStatus.CLOSED);
                                counterOrder.setStatus(OrderStatus.CLOSED);

                                Order sellingOrder, buyingOrder;
                                if (order.getType().equals(OrderType.BUY)) {
                                    buyingOrder = order;
                                    sellingOrder = counterOrder;
                                } else {
                                    sellingOrder = order;
                                    buyingOrder = counterOrder;
                                }


                                return apartmentRepository.findById(sellingOrder.getApartmentId())
                                        .flatMap(apartment -> {
                                            apartment.setOwnerId(buyingOrder.getClientId());
                                            return apartmentRepository.update(apartment);
                                        })
                                        .then(orderRepository.save(order))
                                        .then(orderRepository.save(counterOrder))
                                        .then(tradeRepository.save(trade));
                            });
                });
    }


    public Mono<Trade> createTrade(Order order, Order counterOrder) {
        order.setStatus(OrderStatus.MATCHED);
        counterOrder.setStatus(OrderStatus.MATCHED);

        Trade trade = new Trade();
        trade.setOrderId(order.getId());
        trade.setCounterOrderId(counterOrder.getId());
        trade.setStatus(TradeStatus.PENDING);
        trade.setCreatedAt(Instant.now());
        trade.setUpdatedAt(Instant.now());

        return orderRepository.save(order)
                .then(orderRepository.save(counterOrder))
                .then(tradeRepository.save(trade));
    }

    public Mono<Trade> rejectTrade(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Trade not found" + tradeId)))
                .flatMap(trade -> {
                    trade.setStatus(TradeStatus.REJECTED);
                    trade.setUpdatedAt(Instant.now());

                    Mono<Order> orderMono = orderRepository.findById(trade.getOrderId());
                    Mono<Order> counterMono = orderRepository.findById(trade.getCounterOrderId());

                    return Mono.zip(orderMono, counterMono)
                            .flatMap(tuple -> {
                                Order order = tuple.getT1();
                                Order counterOrder = tuple.getT2();
                                order.setStatus(OrderStatus.OPEN);
                                counterOrder.setStatus(OrderStatus.OPEN);
                                return Mono.zip(orderRepository.save(order), orderRepository.save(counterOrder))
                                        .then(tradeRepository.save(trade));
                            });
                });
    }



}
