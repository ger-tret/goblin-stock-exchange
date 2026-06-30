package me.ger_tret.goblin_stock_exchange;

import org.springframework.boot.SpringApplication;

public class TestGoblinStockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.from(GoblinStockExchangeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
