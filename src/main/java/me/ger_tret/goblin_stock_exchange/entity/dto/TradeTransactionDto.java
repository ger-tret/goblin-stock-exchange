package me.ger_tret.goblin_stock_exchange.entity.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TradeTransactionDto(
        UUID id,
        String buyerName,
        String sellerName,
        String assetTicker,
        Integer quantity,
        BigDecimal price,
        OffsetDateTime executedAt
) {
}
