package me.ger_tret.goblin_stock_exchange.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.TradeTransaction;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderStatus;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderType;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderRequestDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderResponseDto;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.OrderRepository;
import me.ger_tret.goblin_stock_exchange.repository.TradeTransactionRepository;
import me.ger_tret.goblin_stock_exchange.service.AssetService;
import me.ger_tret.goblin_stock_exchange.service.BrokerService;
import me.ger_tret.goblin_stock_exchange.service.InventoryService;
import me.ger_tret.goblin_stock_exchange.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final BrokerService brokerService;
    private final AssetService assetService;
    private final TradeTransactionRepository transactionRepository;
    private final InventoryService inventoryService;

    private final EntityMapper mapper;

    @Override
    @Transactional
    public UUID placeOrder(UUID brokerId, OrderRequestDto request) {
        log.info(">>> NEW ORDER REQUEST: Broker {} wants to {} {} units of asset {} at price {}",
                brokerId, request.type(), request.quantity(), request.assetId(), request.price());

        Broker broker = fetchBroker(brokerId);
        Asset asset = fetchAsset(request.assetId());

        if (request.type() == OrderType.BUY) {
            BigDecimal totalCost = request.price().multiply(BigDecimal.valueOf(request.quantity()));
            log.info("Order type BUY: Checking if broker {} can afford {} gold", brokerId, totalCost);
            brokerService.validateBalance(brokerId, totalCost);
        } else {
            log.info("Order type SELL: Checking if broker {} has {} units in inventory", brokerId, request.quantity());
            inventoryService.validateInventory(brokerId, request.assetId(), request.quantity());
        }

        Order order = Order.builder()
                .broker(broker)
                .asset(asset)
                .orderType(request.type())
                .status(OrderStatus.OPEN)
                .quantity(request.quantity())
                .price(request.price())
                .build();

        log.info("<<< ORDER PLACED: ID={}, Status={}, Broker={}", order.getId(), order.getStatus(), broker.getUsername());
        return orderRepository.save(order).getId();
    }

    @Override
    @Transactional
    public void executeTrade(Order buyOrder, Order sellOrder, Integer quantity, BigDecimal price) {
        log.info("Orchestrating trade execution: {} units at {}", quantity, price);
        log.info("Matching BuyOrder[{}] with SellOrder[{}]", buyOrder.getId(), sellOrder.getId());

        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(quantity));
        UUID buyerId = buyOrder.getBroker().getId();
        UUID sellerId = sellOrder.getBroker().getId();
        UUID assetId = buyOrder.getAsset().getId();

        log.info("Transferring {} gold: {} -> {}", totalCost, buyerId, sellerId);
        brokerService.updateBalance(buyerId, totalCost.negate());
        brokerService.updateBalance(sellerId, totalCost);

        log.info("Transferring {} units of Asset[{}]: {} -> {}", quantity, assetId, sellerId, buyerId);
        inventoryService.updateAssetQuantity(buyerId, assetId, quantity);
        inventoryService.updateAssetQuantity(sellerId, assetId, -quantity);

        TradeTransaction transaction = TradeTransaction.builder()
                .buyOrder(buyOrder)
                .sellOrder(sellOrder)
                .quantity(quantity)
                .price(price)
                .build();
        log.info("Transaction recorded: TX_ID={}", transaction.getId());
        transactionRepository.save(transaction);


        completeOrder(buyOrder);
        completeOrder(sellOrder);

    }

    @Override
    @Transactional
    public void cancelOrder(UUID orderId) {
        log.info("Request to CANCEL order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GseException("Order not found"));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new GseException("Cannot cancel order in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        log.info("Order {} is now CANCELLED", orderId);
    }

    @Override
    public List<OrderResponseDto> getOrdersByBroker(UUID brokerId) {
        return orderRepository.findAllByBrokerId(brokerId).stream()
                .map(mapper::toOrderResponseDto)
                .toList();
    }


    private void completeOrder(Order order) {
        order.setStatus(OrderStatus.FILLED);
        orderRepository.save(order);
    }

    private Broker fetchBroker(UUID id) {
        return brokerService.getBrokerById(id);
    }

    private Asset fetchAsset(UUID id) {
        return assetService.findAssetById(id);
    }
}