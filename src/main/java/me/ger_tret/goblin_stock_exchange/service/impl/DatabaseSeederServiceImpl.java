package me.ger_tret.goblin_stock_exchange.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.enums.BrokerType;
import me.ger_tret.goblin_stock_exchange.repository.AssetRepository;
import me.ger_tret.goblin_stock_exchange.repository.BrokerRepository;
import me.ger_tret.goblin_stock_exchange.service.DatabaseSeederService;
import me.ger_tret.goblin_stock_exchange.service.InventoryService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeederServiceImpl implements DatabaseSeederService {

    private final AssetRepository assetRepository;
    private final BrokerRepository brokerRepository;
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;


    @Override
    @Transactional
    public void seedAssets() {
        if (!assetRepository.findAll().isEmpty()) return;

        try {
            InputStream inputStream = new ClassPathResource("data/assets.json").getInputStream();
            List<Asset> assets = objectMapper.readValue(inputStream, new TypeReference<List<Asset>>() {
            });
            assetRepository.saveAll(assets);
            log.info("Successfully seeded {} assets from JSON", assets.size());
        } catch (IOException e) {
            log.error("Failed to seed assets: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void seedBots() {
        if (!brokerRepository.findAllByBrokerType(BrokerType.BOT).isEmpty()) return;

        try {
            InputStream inputStream = new ClassPathResource("data/bots.json").getInputStream();
            List<Broker> bots = objectMapper.readValue(inputStream, new TypeReference<List<Broker>>() {
            });

            bots.forEach(bot -> {
                bot.setBrokerType(BrokerType.BOT);
                if (bot.getGoldBalance() == null) bot.setGoldBalance(new BigDecimal("1000.0"));
                brokerRepository.save(bot);
                seedInitialInventoryForBot(bot);
            });
            log.info("Successfully seeded {} bots from JSON", bots.size());
        } catch (IOException e) {
            log.error("Failed to seed bots: {}", e.getMessage());
        }
    }

    private void seedInitialInventoryForBot(Broker bot) {
        assetRepository.findAll().stream()
                .limit(4)
                .forEach(asset -> inventoryService.updateAssetQuantity(bot.getId(), asset.getId(), 50));
    }


}
