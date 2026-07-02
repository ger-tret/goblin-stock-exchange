package me.ger_tret.goblin_stock_exchange.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.ger_tret.goblin_stock_exchange.entity.dto.InventoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    void updateAssetQuantity(UUID brokerId, UUID assetId, Integer amountChange);

    Integer getAssetQuantity(UUID brokerId, UUID assetId);

    void validateInventory(UUID brokerId, @NotNull(message = "Asset ID cannot be null") UUID uuid, @NotNull(message = "Quantity is required") @Positive(message = "Quantity must be greater than zero") Integer quantity);

    List<InventoryResponseDto> getBrokerInventory(UUID brokerId);

    public Integer getAvailableQuantity(UUID brokerId, UUID assetId);

    public void lockAssets(UUID brokerId, UUID assetId, Integer qty);

    public void unlockAssets(UUID brokerId, UUID assetId, Integer qty);

}
