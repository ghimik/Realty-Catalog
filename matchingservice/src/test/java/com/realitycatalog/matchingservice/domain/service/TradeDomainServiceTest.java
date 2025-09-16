package com.realitycatalog.matchingservice.domain.service;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TradeDomainServiceTest {

    private TradeRepository tradeRepository;
    private OrderRepository orderRepository;
    private ApartmentRepository apartmentRepository;
    private TradeDomainService service;

    @BeforeEach
    void setUp() {
        tradeRepository = Mockito.mock(TradeRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        apartmentRepository = Mockito.mock(ApartmentRepository.class);
        service = new TradeDomainService(tradeRepository, orderRepository, apartmentRepository);
    }

    @Test
    void acceptTrade_ShouldExecuteAndCloseOrders() {
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setOrderId(10L);
        trade.setCounterOrderId(20L);

        Order o1 = new Order(); o1.setId(10L);
        Order o2 = new Order(); o2.setId(20L);

        when(tradeRepository.findById(1L)).thenReturn(Mono.just(trade));
        when(orderRepository.findById(10L)).thenReturn(Mono.just(o1));
        when(orderRepository.findById(20L)).thenReturn(Mono.just(o2));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(tradeRepository.save(any(Trade.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.acceptTrade(1L))
                .assertNext(t -> {
                    assert t.getStatus() == TradeStatus.EXECUTED;
                })
                .verifyComplete();
    }

    @Test
    void rejectTrade_ShouldReopenOrders() {
        Trade trade = new Trade();
        trade.setId(2L);
        trade.setOrderId(30L);
        trade.setCounterOrderId(40L);

        Order o1 = new Order(); o1.setId(30L);
        Order o2 = new Order(); o2.setId(40L);

        when(tradeRepository.findById(2L)).thenReturn(Mono.just(trade));
        when(orderRepository.findById(30L)).thenReturn(Mono.just(o1));
        when(orderRepository.findById(40L)).thenReturn(Mono.just(o2));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(tradeRepository.save(any(Trade.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.rejectTrade(2L))
                .assertNext(t -> {
                    assert t.getStatus() == TradeStatus.REJECTED;
                })
                .verifyComplete();
    }

    @Test
    void createTrade_ShouldSetMatchedAndPending() {
        Order o1 = new Order(); o1.setId(1L);
        Order o2 = new Order(); o2.setId(2L);

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(tradeRepository.save(any(Trade.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.createTrade(o1, o2))
                .assertNext(trade -> {
                    assert trade.getStatus() == TradeStatus.PENDING;
                    assert o1.getStatus() == OrderStatus.MATCHED;
                    assert o2.getStatus() == OrderStatus.MATCHED;
                })
                .verifyComplete();
    }
}
