package me.ger_tret.goblin_stock_exchange.service;

import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import me.ger_tret.goblin_stock_exchange.service.strategy.BotStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BotRunnerService {
    @Async("botExecutor")
    public void runStrategyAsync(BotStrategy strategy, BrokerDto bot) {
        log.trace("Bot {} is executing strategy: {}", bot.username(), strategy.getName());
        try {
            strategy.execute(bot);
        } catch (Exception e) {
            log.error("Critical error in bot {} execution: {}", bot.username(), e.getMessage());
        }
    }
}