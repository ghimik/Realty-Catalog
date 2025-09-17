package com.realitycatalog.matchingservice.e2e;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import com.realitycatalog.matchingservice.domain.service.OrderDomainService;
import com.realitycatalog.matchingservice.domain.service.TradeDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class E2EScenarioTest {

    private OrderRepository orderRepository;
    private TradeRepository tradeRepository;
    private ApartmentRepository apartmentRepository;

    private OrderDomainService orderService;
    private TradeDomainService tradeService;

    private final Map<Long, Order> ordersDb = new HashMap<>();
    private final Map<Long, Trade> tradesDb = new HashMap<>();

    @BeforeEach
    void setup() {
        orderRepository = Mockito.mock(OrderRepository.class);
        tradeRepository = Mockito.mock(TradeRepository.class);
        apartmentRepository = Mockito.mock(ApartmentRepository.class);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> {
                    Order o = inv.getArgument(0);
                    if (o.getId() == null) o.setId((long) (ordersDb.size() + 1));
                    ordersDb.put(o.getId(), o);
                    return Mono.just(o);
                });

        when(orderRepository.findById(anyLong()))
                .thenAnswer(inv -> {
                    Long id = inv.getArgument(0);
                    return Mono.justOrEmpty(ordersDb.get(id));
                });

        when(tradeRepository.save(any(Trade.class)))
                .thenAnswer(inv -> {
                    Trade t = inv.getArgument(0);
                    if (t.getId() == null) t.setId((long) (tradesDb.size() + 1));
                    tradesDb.put(t.getId(), t);
                    return Mono.just(t);
                });

        when(tradeRepository.findById(anyLong()))
                .thenAnswer(inv -> {
                    Long id = inv.getArgument(0);
                    return Mono.justOrEmpty(tradesDb.get(id));
                });

        when(orderRepository.findOpenOrdersByTypeAndPriceRange(anyString(), anyLong(), anyLong()))
                .thenAnswer(inv -> {
                    String type = inv.getArgument(0);
                    Long min = inv.getArgument(1);
                    Long max = inv.getArgument(2);
                    return Flux.fromIterable(ordersDb.values())
                            .filter(o -> o.getType().name().equals(type))
                            .filter(o -> o.getStatus() == OrderStatus.OPEN)
                            .filter(o -> o.getPriceMin() >= min && o.getPriceMax() <= max);
                });

        when(apartmentRepository.findById(anyLong()))
                .thenAnswer(inv -> {
                    Long id = inv.getArgument(0);
                    Apartment apt = new Apartment();
                    apt.setId(id);
                    return Mono.just(apt);
                });



        orderService = new OrderDomainService(orderRepository, tradeRepository, apartmentRepository);
        tradeService = new TradeDomainService(tradeRepository, orderRepository, apartmentRepository);

    }


    @Test
    void tradeRejectFlow() {
        Order buy = new Order();
        buy.setType(OrderType.BUY);
        buy.setPriceMin(5_000_000L);
        buy.setPriceMax(7_000_000L);
        buy.setStatus(OrderStatus.OPEN);

        Order sell = new Order();
        sell.setType(OrderType.SELL);
        sell.setPriceMin(6_000_000L);
        sell.setPriceMax(6_000_000L);
        sell.setStatus(OrderStatus.OPEN);

        Mono<Order> savedBuy = orderService.createOrder(buy);
        Mono<Order> savedSell = orderService.createOrder(sell);

        StepVerifier.create(savedBuy).expectNextCount(1).verifyComplete();
        StepVerifier.create(savedSell).expectNextCount(1).verifyComplete();

        Flux<Order> matches = orderService.matchOrder(buy.getId());
        StepVerifier.create(matches)
                .expectNextMatches(o -> o.getType() == OrderType.SELL)
                .verifyComplete();

        Mono<Trade> tradeMono = tradeService.createTrade(buy, sell);
        StepVerifier.create(tradeMono)
                .expectNextMatches(t -> t.getStatus() == TradeStatus.PENDING)
                .verifyComplete();

        Long tradeId = tradesDb.values().iterator().next().getId();

        // отклоняем трейд
        StepVerifier.create(tradeService.rejectTrade(tradeId))
                .expectNextMatches(t -> t.getStatus() == TradeStatus.REJECTED)
                .verifyComplete();

        assert ordersDb.get(buy.getId()).getStatus() == OrderStatus.OPEN;
        assert ordersDb.get(sell.getId()).getStatus() == OrderStatus.OPEN;
    }
}
