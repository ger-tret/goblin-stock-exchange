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

        BigDecimal price = asset.currentPrice().multiply(
                BigDecimal.valueOf(0.9 + ThreadLocalRandom.current().nextDouble() * 0.2)
        );

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

    @Override
    public String getName() {
        return "MR/MRS. RANDOMNESS";
    }


}
