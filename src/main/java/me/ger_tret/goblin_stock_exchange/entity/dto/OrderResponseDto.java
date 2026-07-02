package me.ger_tret.goblin_stock_exchange.entity.dto;

import me.ger_tret.goblin_stock_exchange.entity.enums.OrderStatus;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        UUID assetId,
        String assetName,
        OrderType type,
        OrderStatus status,
        Integer quantity,
        BigDecimal price,
        OffsetDateTime createdAt
) {
}