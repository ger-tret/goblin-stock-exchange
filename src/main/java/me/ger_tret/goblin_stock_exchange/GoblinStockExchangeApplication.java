package me.ger_tret.goblin_stock_exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class GoblinStockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoblinStockExchangeApplication.class, args);
	}

}
