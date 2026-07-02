package me.ger_tret.goblin_stock_exchange.entity.dto;

import jakarta.validation.constraints.*;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderType;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequestDto(
        @NotNull(message = "Asset ID cannot be null")
        UUID assetId,

        @NotNull(message = "Order type is required")
        OrderType type,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0001", message = "Price must be positive")
        BigDecimal price
) {}