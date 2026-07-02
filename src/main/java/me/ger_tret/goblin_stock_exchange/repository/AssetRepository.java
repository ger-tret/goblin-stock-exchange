package me.ger_tret.goblin_stock_exchange.repository;

import me.ger_tret.goblin_stock_exchange.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    Optional<Asset> findByTicker(String ticker);

    @Query("SELECT DISTINCT a FROM Asset a JOIN Order o ON o.asset = a WHERE o.status = 'OPEN'")
    List<Asset> findAllWithOpenOrder();
}
