package me.ger_tret.goblin_stock_exchange.service.impl;


import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.Inventory;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.repository.AssetRepository;
import me.ger_tret.goblin_stock_exchange.repository.BrokerRepository;
import me.ger_tret.goblin_stock_exchange.repository.InventoryRepository;
import me.ger_tret.goblin_stock_exchange.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final BrokerRepository brokerRepository;
    private final AssetRepository assetRepository;

    @Override
    public void updateAssetQuantity(UUID brokerId, UUID assetId, Integer amountChange) {
        Inventory inv = inventoryRepository.findByBrokerIdAndAssetId(brokerId, assetId)
                .orElseGet(() -> {
                    Broker broker = brokerRepository.findById(brokerId).orElseThrow();
                    Asset asset = assetRepository.findById(assetId).orElseThrow();
                    return Inventory.builder().broker(broker).asset(asset).quantity(0).build();
                });

        int newQty = inv.getQuantity() + amountChange;
        if (newQty < 0) {
            throw new GseException("Not enough assets in inventory!");
        }
        inv.setQuantity(newQty);
        inventoryRepository.save(inv);
    }

    public void validateInventory(UUID brokerId, UUID assetId, Integer requiredQty) {
        Integer currentQty = inventoryRepository.findByBrokerIdAndAssetId(brokerId, assetId)
                .map(Inventory::getQuantity)
                .orElse(0);

        if (currentQty < requiredQty) {
            throw new GseException("Insufficient assets in inventory");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAssetQuantity(UUID brokerId, UUID assetId) {
        return inventoryRepository.findByBrokerIdAndAssetId(brokerId, assetId)
                .map(Inventory::getQuantity)
                .orElse(0);
    }
}
