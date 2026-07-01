package me.ger_tret.goblin_stock_exchange.entity.dto;

import me.ger_tret.goblin_stock_exchange.entity.constant.OrderStatus;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderType;

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