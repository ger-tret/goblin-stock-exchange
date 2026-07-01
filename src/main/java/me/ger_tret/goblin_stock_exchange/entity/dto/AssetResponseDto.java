package me.ger_tret.goblin_stock_exchange.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record AssetResponseDto(
        @Schema(description = "Unique identifier of the asset", example = "1")
        UUID id,

        @Schema(description = "Asset name", example = "Troll Slime")
        String name,

        @Schema(description = "Short asset name", example = "TSS")
        String ticker,

        @Schema(description = "Current market price in gold tokens")
        BigDecimal currentPrice
) {
}
