package me.ger_tret.goblin_stock_exchange.service;


import me.ger_tret.goblin_stock_exchange.entity.dto.OrderRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    UUID placeOrder(UUID brokerId, OrderRequestDto request);

    void cancelOrder(UUID orderId);

    List<OrderRequestDto> getOrdersByBroker(UUID brokerId);

    void executeOrder(UUID orderId);
}
