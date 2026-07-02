package me.ger_tret.goblin_stock_exchange.exception;

import java.math.BigDecimal;

public class InvalidOrderPriceException extends GseException {
    public InvalidOrderPriceException(BigDecimal price, BigDecimal marketPrice) {
        super(String.format("Price %s is too far from market price %s. Goblins suspect a scam!",
                price.toPlainString(), marketPrice.toPlainString()));
    }
}
