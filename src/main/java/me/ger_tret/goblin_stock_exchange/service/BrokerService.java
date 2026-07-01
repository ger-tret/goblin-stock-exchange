package me.ger_tret.goblin_stock_exchange.service;

import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface BrokerService {
    BrokerDto getBrokerById(UUID id);
    BrokerDto getBrokerByUsername(String username);
    BrokerDto registerBroker(String username);
    void updateBalance(UUID id, BigDecimal amount);
}
