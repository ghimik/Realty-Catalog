package com.realitycatalog.matchingservice.domain.service;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderDomainServiceTest {

    private OrderRepository orderRepository;
    private TradeRepository tradeRepository;
    private ApartmentRepository apartmentRepository;
    private OrderDomainService service;

    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        tradeRepository = Mockito.mock(TradeRepository.class);
        apartmentRepository = Mockito.mock(ApartmentRepository.class);
        service = new OrderDomainService(orderRepository, tradeRepository, apartmentRepository);
    }

    @Test
    void createOrder_ShouldSetDefaultsAndSave() {
        Order order = new Order();
        order.setType(OrderType.BUY);

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.createOrder(order))
                .assertNext(saved -> {
                    assert saved.getStatus() == OrderStatus.OPEN;
                    assert saved.getCreatedAt() != null;
                    assert saved.getUpdatedAt() != null;
                })
                .verifyComplete();

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void matchOrder_ShouldReturnSellOrdersForBuyOrder() {
        Order buyOrder = new Order();
        buyOrder.setType(OrderType.BUY);
        buyOrder.setPriceMin(100L);
        buyOrder.setPriceMax(200L);

        when(orderRepository.findOpenOrdersByTypeAndPriceRange(eq("SELL"), anyLong(), anyLong()))
                .thenReturn(Flux.just(new Order()));

        StepVerifier.create(service.matchOrder(buyOrder))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void executeTrade_ShouldCreateTrade() {
        Order order = new Order();
        order.setId(1L);
        Order counter = new Order();
        counter.setId(2L);

        when(tradeRepository.save(any(Trade.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.executeTrade(order, counter))
                .assertNext(trade -> {
                    assert trade.getOrderId() == 1L;
                    assert trade.getCounterOrderId() == 2L;
                    assert trade.getStatus() == TradeStatus.PENDING;
                })
                .verifyComplete();
    }

    @Test
    void apartmentsMatch_ShouldCompareCorrectly() {
        Apartment a1 = new Apartment();
        a1.setRooms(2);
        a1.setArea(50.0);
        a1.setFloor(3);

        Apartment a2 = new Apartment();
        a2.setRooms(2);
        a2.setArea(50.0);
        a2.setFloor(3);

        when(apartmentRepository.findById(1L)).thenReturn(Mono.just(a1));
        when(apartmentRepository.findById(2L)).thenReturn(Mono.just(a2));

        assertTrue(service.apartmentsMatchReactive(1L, 2L).block());


    }
}
