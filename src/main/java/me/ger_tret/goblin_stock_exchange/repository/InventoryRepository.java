package me.ger_tret.goblin_stock_exchange.repository;

import jakarta.persistence.LockModeType;
import me.ger_tret.goblin_stock_exchange.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByBrokerIdAndAssetId(UUID brokerId, UUID assetId);

    List<Inventory> findAllByBrokerId(UUID brokerId);
}
