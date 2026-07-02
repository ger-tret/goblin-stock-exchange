package me.ger_tret.goblin_stock_exchange.repository;

import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderStatus;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByBrokerId(UUID brokerId);

    List<Order> findAllByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.asset.id = :assetId AND o.orderType = :type AND o.status = :status")
    List<Order> findTopOrders(
            @Param("assetId") UUID assetId,
            @Param("type") OrderType type,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
