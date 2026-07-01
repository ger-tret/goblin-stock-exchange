package me.ger_tret.goblin_stock_exchange.exception;

import java.util.UUID;

public class InsufficientGoldException extends GseException {
    public InsufficientGoldException(UUID brokerId) {
        super("Goblin " + brokerId + " is too poor for this trade!");
    }
}