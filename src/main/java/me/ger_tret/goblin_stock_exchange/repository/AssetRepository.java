package me.ger_tret.goblin_stock_exchange.repository;

import me.ger_tret.goblin_stock_exchange.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    Optional<Asset> findByTicker(String ticker);
}
