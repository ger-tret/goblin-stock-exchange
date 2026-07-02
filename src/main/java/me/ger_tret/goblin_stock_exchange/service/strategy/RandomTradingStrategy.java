package me.ger_tret.goblin_stock_exchange.service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderType;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderRequestDto;
import me.ger_tret.goblin_stock_exchange.service.AssetService;
import me.ger_tret.goblin_stock_exchange.service.OrderService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;


@Component
@RequiredArgsConstructor
@Slf4j
public class RandomTradingStrategy implements BotStrategy {
    private final OrderService orderService;
    private final AssetService assetService;

    @Override
    public void execute(BrokerDto bot) {
            var assets = assetService.getAllAssets();
            if (assets.isEmpty()) return;

            var asset = assets.get(ThreadLocalRandom.current().nextInt(assets.size()));
            boolean isBuy = ThreadLocalRandom.current().nextBoolean();

            BigDecimal price = generatePrice(asset.currentPrice());

            int quantity;
            if (isBuy) {
                BigDecimal maxAffordable = bot.goldBalance().divide(price, 0, RoundingMode.DOWN);
                int maxQty = maxAffordable.intValue();

                if (maxQty <= 0) return;

                quantity = ThreadLocalRandom.current().nextInt(1, Math.min(maxQty, 10) + 1);
            } else {
                quantity = ThreadLocalRandom.current().nextInt(1, 11);
            }

            orderService.placeOrder(bot.id(), new OrderRequestDto(asset.id(), isBuy ? OrderType.BUY : OrderType.SELL, quantity, price));

            OrderRequestDto requestDto = new OrderRequestDto(
                    asset.id(),
                    isBuy ? OrderType.BUY : OrderType.SELL,
                    ThreadLocalRandom.current().nextInt(1, 10),
                    price
            );

            try {
                orderService.placeOrder(bot.id(), requestDto);
            } catch (Exception e) {
                log.trace("Bot {} failed to place order: {}", bot.username(), e.getMessage());
            }
        }

    private BigDecimal generatePrice(BigDecimal current) {
        double modifier = 0.95 + (ThreadLocalRandom.current().nextDouble() * 0.1); // +- 5%
        return current.multiply(BigDecimal.valueOf(modifier)).setScale(4, RoundingMode.HALF_UP);
    }

    @Override
    public String getName() {
        return "MR/MRS. RANDOMNESS";
    }


}
