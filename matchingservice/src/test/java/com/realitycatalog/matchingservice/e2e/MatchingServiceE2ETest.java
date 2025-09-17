package com.realitycatalog.matchingservice.e2e;

import com.realitycatalog.matchingservice.domain.model.*;
import com.realitycatalog.matchingservice.domain.port.ApartmentRepository;
import com.realitycatalog.matchingservice.domain.port.OrderRepository;
import com.realitycatalog.matchingservice.domain.port.TradeRepository;
import com.realitycatalog.matchingservice.domain.service.OrderDomainService;
import com.realitycatalog.matchingservice.domain.service.TradeDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class MatchingServiceE2ETest {

    private InMemoryOrderRepository orderRepo;
    private InMemoryTradeRepository tradeRepo;
    private InMemoryApartmentRepository apartmentRepo;

    private OrderDomainService orderService;
    private TradeDomainService tradeService;

    @BeforeEach
    void setup() {
        orderRepo = new InMemoryOrderRepository();
        tradeRepo = new InMemoryTradeRepository();
        apartmentRepo = new InMemoryApartmentRepository();

        orderService = new OrderDomainService(orderRepo, tradeRepo, apartmentRepo);
        tradeService = new TradeDomainService(tradeRepo, orderRepo, apartmentRepo);
    }

    @Test
    void acceptTrade_ShouldExecuteAndCloseOrders() {
        // given: апартаменты
        Apartment apt1 = new Apartment();
        apt1.setId(100L);
        apt1.setOwnerId(1L);
        apartmentRepo.save(apt1);

        Apartment apt2 = new Apartment();
        apt2.setId(200L);
        apt2.setOwnerId(2L);
        apartmentRepo.save(apt2);

        // ордер BUY
        Order buy = new Order();
        buy.setId(10L);
        buy.setType(OrderType.BUY);
        buy.setStatus(OrderStatus.OPEN);
        buy.setApartmentId(100L);
        buy.setClientId(1L);
        orderRepo.save(buy).block();

        // ордер SELL
        Order sell = new Order();
        sell.setId(20L);
        sell.setType(OrderType.SELL);
        sell.setStatus(OrderStatus.OPEN);
        sell.setApartmentId(200L);
        sell.setClientId(2L);
        orderRepo.save(sell).block();

        // трейд PENDING
        Trade trade = new Trade();
        trade.setId(1L);
        trade.setOrderId(10L);
        trade.setCounterOrderId(20L);
        trade.setStatus(TradeStatus.PENDING);
        tradeRepo.save(trade).block();

        // when: принимаем трейд
        StepVerifier.create(tradeService.acceptTrade(1L))
                .assertNext(t -> {
                    assert t.getStatus() == TradeStatus.EXECUTED;
                })
                .verifyComplete();

        // then: оба ордера должны быть CLOSED
        StepVerifier.create(orderRepo.findById(10L))
                .expectNextMatches(o -> o.getStatus() == OrderStatus.CLOSED)
                .verifyComplete();

        StepVerifier.create(orderRepo.findById(20L))
                .expectNextMatches(o -> o.getStatus() == OrderStatus.CLOSED)
                .verifyComplete();
    }




    @Test
    void fullBuySellFlow() {
        // given: квартира с владельцем 1
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        apartment.setOwnerId(1L);
        apartment.setRooms(2);
        apartmentRepo.save(apartment);

        // ордер SELL от клиента 1
        Order sell = new Order();
        sell.setId(10L);
        sell.setType(OrderType.SELL);
        sell.setClientId(1L);
        sell.setApartmentId(1L);
        sell.setPriceMin(100L);
        sell.setPriceMax(200L);

        // ордер BUY от клиента 2
        Order buy = new Order();
        buy.setId(11L);
        buy.setType(OrderType.BUY);
        buy.setClientId(2L);
        buy.setApartmentId(null);
        buy.setPriceMin(100L);
        buy.setPriceMax(200L);

        // when: создаем ордера
        StepVerifier.create(orderService.createOrder(sell))
                .expectNextMatches(o -> o.getStatus() == OrderStatus.OPEN)
                .verifyComplete();
        StepVerifier.create(orderService.createOrder(buy))
                .expectNextMatches(o -> o.getStatus() == OrderStatus.OPEN)
                .verifyComplete();

        // when: матчим BUY ордер
        StepVerifier.create(orderService.matchOrder(buy))
                .expectNextMatches(o -> o.getId().equals(10L))
                .verifyComplete();

        // when: создаем трейд
        StepVerifier.create(tradeService.createTrade(buy, sell))
                .expectNextMatches(t -> t.getStatus() == TradeStatus.PENDING)
                .verifyComplete();

        // then: принимаем трейд
        StepVerifier.create(tradeService.acceptTrade(1L))
                .expectNextMatches(t -> t.getStatus() == TradeStatus.EXECUTED)
                .verifyComplete();

        // then: квартира должна перейти к клиенту 2
        StepVerifier.create(apartmentRepo.findById(1L))
                .expectNextMatches(a -> a.getOwnerId().equals(2L))
                .verifyComplete();
    }

    static class InMemoryOrderRepository implements OrderRepository {
        private final Map<Long, Order> store = new HashMap<>();
        public Mono<Order> save(Order order) { store.put(order.getId(), order); return Mono.just(order); }
        public Mono<Order> findById(Long id) { return Mono.justOrEmpty(store.get(id)); }
        public Flux<Order> findAll() { return Flux.fromIterable(store.values()); }
        public Mono<Void> deleteById(Long id) { store.remove(id); return Mono.empty(); }
        public Flux<Order> findOpenOrdersByTypeAndPriceRange(String type, Long min, Long max) {
            return Flux.fromStream(store.values().stream()
                    .filter(o -> o.getStatus() == OrderStatus.OPEN)
                    .filter(o -> o.getType().name().equals(type))
                    .filter(o -> o.getPriceMin() >= min && o.getPriceMax() <= max));
        }
        public Flux<Order> findOpenExchangeOrders() { return Flux.empty(); }
        public Flux<Order> findByClientId(Long clientId) {
            return Flux.fromIterable(store.values())
                    .filter(o -> Objects.equals(o.getClientId(), clientId));
        }
    }

    static class InMemoryTradeRepository implements TradeRepository {
        private final Map<Long, Trade> store = new HashMap<>();
        private final AtomicLong seq = new AtomicLong(1);
        public Mono<Trade> save(Trade trade) {
            if (trade.getId() == null) trade.setId(seq.getAndIncrement());
            store.put(trade.getId(), trade);
            return Mono.just(trade);
        }
        public Mono<Trade> findById(Long id) { return Mono.justOrEmpty(store.get(id)); }
        public Flux<Trade> findAll() { return Flux.fromIterable(store.values()); }
        public Flux<Trade> findByClients(Long c1, Long c2) {
            return Flux.fromStream(store.values().stream()
                    .filter(t -> t.getOrderId().equals(c1) || t.getCounterOrderId().equals(c2)));
        }
    }

    static class InMemoryApartmentRepository implements ApartmentRepository {
        private final Map<Long, Apartment> store = new HashMap<>();
        public Mono<Apartment> findById(Long id) { return Mono.justOrEmpty(store.get(id)); }
        public Mono<Apartment> update(Apartment a) { store.put(a.getId(), a); return Mono.just(a); }
        public void save(Apartment a) { store.put(a.getId(), a); }
    }
}
