package me.ger_tret.goblin_stock_exchange.service.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.constant.OrderType;
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
public class LiquidityProviderStrategy implements BotStrategy {
    private final OrderService orderService;
    private final AssetService assetService;

    @Override
    public void execute(BrokerDto bot) {
        assetService.getAllAssets().forEach(asset -> {
                    BigDecimal basePrice = asset.currentPrice();


                    orderService.placeOrder(bot.id(), new OrderRequestDto(
                            asset.id(),
                            OrderType.BUY,
                            5,
                            basePrice.multiply(new BigDecimal("0.98"))
                    ));

                    orderService.placeOrder(bot.id(), new OrderRequestDto(
                            asset.id(),
                            OrderType.SELL,
                            ThreadLocalRandom.current().nextInt(assetService.getAllAssets().size()),
                            basePrice.multiply(new BigDecimal("1.02"))
                    ));
                }
        );
    }

    @Override
    public String getName() {
        return "MARKET_MAKER";
    }

}
