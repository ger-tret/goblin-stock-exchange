package me.ger_tret.goblin_stock_exchange.service;


import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderRequestDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    UUID placeOrder(UUID brokerId, OrderRequestDto request);

    void cancelOrder(UUID orderId);

    List<OrderResponseDto> getOrdersByBroker(UUID brokerId);

    void executeTrade(Order buyOrder, Order sellOrder, Integer quantity, BigDecimal price);
}
