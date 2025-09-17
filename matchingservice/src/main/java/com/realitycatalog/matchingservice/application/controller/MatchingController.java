package com.realitycatalog.matchingservice.application.controller;

import com.realitycatalog.matchingservice.application.dto.OrderDto;
import com.realitycatalog.matchingservice.application.dto.TradeDto;
import com.realitycatalog.matchingservice.application.service.MatchingApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingApplicationService service;

    @PostMapping("/orders")
    public Mono<OrderDto> createOrder(@RequestBody OrderDto dto) {
        return service.createOrder(dto);
    }

    @GetMapping("/orders/{id}/match")
    public Flux<OrderDto> matchOrder(@PathVariable Long id) {
        return service.matchOrder(id);
    }

    @PostMapping("/trades/{tradeId}/accept")
    public Mono<TradeDto> acceptTrade(@PathVariable Long tradeId) {
        return service.acceptTrade(tradeId);
    }

    @PostMapping("/trades/{tradeId}/reject")
    public Mono<TradeDto> rejectTrade(@PathVariable Long tradeId) {
        return service.rejectTrade(tradeId);
    }

    @PostMapping("/trades")
    public Mono<TradeDto> createTrade(@RequestParam Long orderId, @RequestParam Long counterOrderId) {
        return service.createTrade(orderId, counterOrderId);
    }

    @GetMapping("/orders")
    public Flux<OrderDto> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Mono<OrderDto> getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @DeleteMapping("/orders/{id}")
    public Mono<Void> deleteOrderById(@PathVariable Long id) {
        return service.deleteOrder(id);
    }

    @GetMapping("/trades")
    public Flux<TradeDto> getAllTrades() {
        return service.getAllTrades();
    }

    @GetMapping("/trades/{id}")
    public Mono<TradeDto> getTradeById(@PathVariable Long id) {
        return service.getTradeById(id);
    }

    @GetMapping("/orders/search")
    public Flux<OrderDto> getOrders(@RequestParam Long clientId) {
        return service.getOrders(clientId);
    }

    @GetMapping("/trades/search")
    public Flux<TradeDto> getTrades(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long counterClientId) {
        return service.getTrades(clientId, counterClientId);
    }


}
