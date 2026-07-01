package me.ger_tret.goblin_stock_exchange.entity.dto;

import java.util.UUID;

public record InventoryResponseDto(
        UUID assetId,
        String assetName,
        String ticker,
        Integer quantity
) {
}
