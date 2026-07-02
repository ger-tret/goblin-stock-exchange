package me.ger_tret.goblin_stock_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderStatus;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderType;
import me.ger_tret.goblin_stock_exchange.repository.AssetRepository;
import me.ger_tret.goblin_stock_exchange.repository.OrderRepository;
import me.ger_tret.goblin_stock_exchange.service.MatchingService;
import me.ger_tret.goblin_stock_exchange.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Override
    @Scheduled(fixedDelayString = "{gse.matching.interval}")
    public void performMatching() {
        log.debug("Starting matching cycle...");

        List<Asset> activeAssets = assetRepository.findAllWithOpenOrder();

        activeAssets.parallelStream().forEach(this::processAsset);
    }

    private void processAsset(Asset asset) {
        log.trace("Processing orders for asset: {}", asset.getTicker());

        List<Order> buyOrders = orderRepository.findTopOrders(
                asset.getId(),
                OrderType.BUY,
                OrderStatus.OPEN,
                PageRequest.of(0, 100, Sort.by
                                (Sort.Direction.DESC, "price")
                        .and(Sort.by(Sort.Direction.ASC, "createdAt")))
        );

        List<Order> sellOrders = orderRepository.findTopOrders(
                asset.getId(),
                OrderType.SELL,
                OrderStatus.OPEN,
                PageRequest.of(0, 100, Sort.by
                                (Sort.Direction.ASC, "price")
                        .and(Sort.by(Sort.Direction.ASC, "createdAt")))
        );

        if (buyOrders.isEmpty() || sellOrders.isEmpty()) return;

        int bIdx = 0;
        int sIdx = 0;

        while (bIdx < buyOrders.size() && sIdx < sellOrders.size()) {
            Order buy = buyOrders.get(bIdx);
            Order sell = sellOrders.get(sIdx);


            if (buy.getPrice().compareTo(sell.getPrice()) >= 0) {
                try {
                    BigDecimal matchPrice = sell.getPrice();
                    int matchQty = Math.min(buy.getQuantity(), sell.getQuantity());


                    orderService.executeTrade(buy, sell, matchQty, matchPrice);

                    log.info("Match found for {}: {} units @ {}", asset.getTicker(), matchQty, matchPrice);

                    bIdx++;
                    sIdx++;
                } catch (Exception e) {
                    log.error("Match execution failed for asset {}: {}", asset.getTicker(), e.getMessage());
                    break;
                }
            } else {
                break;
            }
        }

    }

}
