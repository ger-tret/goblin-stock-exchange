package me.ger_tret.goblin_stock_exchange.repository;

import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByBrokerId(UUID brokerId);
    List<Order> findAllByStatus(OrderStatus status);
}
