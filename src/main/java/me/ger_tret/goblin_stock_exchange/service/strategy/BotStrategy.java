package me.ger_tret.goblin_stock_exchange.service.strategy;

import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;

public interface BotStrategy {
    void execute(BrokerDto bot);
    String getName();
}
