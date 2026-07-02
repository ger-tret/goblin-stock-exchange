package me.ger_tret.goblin_stock_exchange.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ger_tret.goblin_stock_exchange.service.DatabaseSeederService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final DatabaseSeederService seederService;


    @Override
    @Transactional
    public void run(String... args){
        log.info("=Starting database seeding=");
        seederService.seedAssets();
        seederService.seedBots();
        log.info("=Database seeding successfully");
    }
}
