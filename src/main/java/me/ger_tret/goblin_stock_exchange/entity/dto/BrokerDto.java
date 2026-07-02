package me.ger_tret.goblin_stock_exchange.entity.dto;

import me.ger_tret.goblin_stock_exchange.entity.constant.BrokerType;

import java.math.BigDecimal;
import java.util.UUID;

public record BrokerDto(
        UUID id,
        String username,
        BigDecimal goldbalance,
        BrokerType brokerType,
        Integer reputation
) {
}
