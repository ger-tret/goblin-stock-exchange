package me.ger_tret.goblin_stock_exchange.entity.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BrokerDto(
        UUID id,
        String username,
        BigDecimal goldbalance,
        Integer reputation
) {
}
