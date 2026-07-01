package me.ger_tret.goblin_stock_exchange.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public interface InventoryService {
    void updateAssetQuantity(UUID brokerId, UUID assetId, Integer amountChange);

    Integer getAssetQuantity(UUID brokerId, UUID assetId);

    void validateInventory(UUID brokerId, @NotNull(message = "Asset ID cannot be null") UUID uuid, @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be greater than zero") Integer quantity);

}
