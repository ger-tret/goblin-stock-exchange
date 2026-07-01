package me.ger_tret.goblin_stock_exchange.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "trade_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order sellOrder;

    private Integer quantity;
    private BigDecimal price;
    private OffsetDateTime executedAt;

    @PrePersist
    protected void onExecute() {
        this.executedAt = OffsetDateTime.now();
    }
}