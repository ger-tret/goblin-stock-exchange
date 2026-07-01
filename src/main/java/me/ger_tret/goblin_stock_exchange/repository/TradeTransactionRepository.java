package me.ger_tret.goblin_stock_exchange.repository;

import me.ger_tret.goblin_stock_exchange.entity.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, UUID> {
    List<TradeTransaction> findTop50ByOrderByExecutedAtDesc();
}
