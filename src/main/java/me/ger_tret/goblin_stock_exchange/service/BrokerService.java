package me.ger_tret.goblin_stock_exchange.service;

import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface BrokerService {
    BrokerDto getBrokerDtoById(UUID id);

    BrokerDto getBrokerByUsername(String username);

    BrokerDto registerBroker(String username);

    void updateBalance(UUID id, BigDecimal amount);

    public void lockGold(UUID brokerId, BigDecimal amount);

    public void unlockGold(UUID brokerId, BigDecimal amount);

    void validateBalance(UUID brokerId, BigDecimal totalCost);

    Broker getBrokerById(UUID id);

    public BigDecimal getAvailableGold(UUID id);
}
