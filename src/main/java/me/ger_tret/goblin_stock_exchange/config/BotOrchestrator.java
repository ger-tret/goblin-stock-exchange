package me.ger_tret.goblin_stock_exchange.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.constant.BrokerType;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.BrokerRepository;
import me.ger_tret.goblin_stock_exchange.service.BotRunnerService;
import me.ger_tret.goblin_stock_exchange.service.strategy.BotStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotOrchestrator {
    private final BrokerRepository brokerRepository;
    private final List<BotStrategy> botStrategyList;
    private final EntityMapper mapper;
    private final BotRunnerService botRunner;

    @Scheduled(fixedDelayString = "${gse.simulation.interval:10000} ")
    public void awakeBots(){
        List<Broker> botList = brokerRepository.findAllByBrokerType(BrokerType.BOT);

        if (botList.isEmpty()) {
            log.warn("No bots found in database. Check DataInitializer");
            return;
        }

        log.debug("Waking up {} bots", botList.size());

        for(Broker bot : botList){
            BrokerDto brokerDto = mapper.toDto(bot);
            BotStrategy strategy = botStrategyList.get(ThreadLocalRandom.current().nextInt(botStrategyList.size()));

            botRunner.runStrategyAsync(strategy, brokerDto);
        }
    }

    @Async("botExecutor")
    public void runStrategyAsync(BotStrategy strategy, BrokerDto bot){
        strategy.execute(bot);
    }


}
