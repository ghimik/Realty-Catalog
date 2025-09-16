package com.realitycatalog.matchingservice.domain.service;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final ApartmentRepository apartmentRepository;

    public Mono<Order> createOrder(Order order) {
        order.setStatus(order.getStatus() == null ? OrderStatus.OPEN : order.getStatus());
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        return orderRepository.save(order);
    }

    public Flux<Order> matchOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found" + orderId)))
                .flatMapMany(this::matchOrder);
    }


    public Mono<Trade> executeTrade(Order order, Order counterOrder) {
        Trade trade = new Trade();
        trade.setOrderId(order.getId());
        trade.setCounterOrderId(counterOrder.getId());
        trade.setStatus(TradeStatus.PENDING);
        trade.setCreatedAt(Instant.now());
        trade.setUpdatedAt(Instant.now());
        return tradeRepository.save(trade);
    }
    public Flux<Order> matchOrder(Order order) {
        if (order.getType() == OrderType.BUY) {
            return orderRepository.findOpenOrdersByTypeAndPriceRange("SELL", order.getPriceMin(), order.getPriceMax());
        } else if (order.getType() == OrderType.SELL) {
            return orderRepository.findOpenOrdersByTypeAndPriceRange("BUY", order.getPriceMin(), order.getPriceMax());
        } else {
            return orderRepository.findOpenExchangeOrders()
                    .flatMap(o -> apartmentsMatchReactive(order.getApartmentId(), o.getApartmentId())
                            .filter(matches -> matches)
                            .map(matches -> o)
                    );
        }
    }

    Mono<Boolean> apartmentsMatchReactive(Long apartmentId1, Long apartmentId2) {
        Mono<Apartment> a1 = apartmentRepository.findById(apartmentId1);
        Mono<Apartment> a2 = apartmentRepository.findById(apartmentId2);

        return Mono.zip(a1, a2)
                .map(tuple -> {
                    Apartment apt1 = tuple.getT1();
                    Apartment apt2 = tuple.getT2();
                    return apt1.getRooms().equals(apt2.getRooms()) &&
                            apt1.getArea().equals(apt2.getArea()) &&
                            apt1.getFloor().equals(apt2.getFloor());
                });
    }


}