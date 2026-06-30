package me.ger_tret.goblin_stock_exchange.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record AssetResponseDto(
        @Schema(description = "Unique identifier of the asset", example = "1")
        Long id,

        @Schema(description = "Asset name", example = "Troll Slime")
        String name,

        @Schema(description = "Current market price in gold tokens")
        BigDecimal currentPrice
) {}
