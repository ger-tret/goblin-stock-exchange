package me.ger_tret.goblin_stock_exchange.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "brokers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "gold_balance", nullable = false)
    private BigDecimal goldBalance;

    private Integer reputation;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = OffsetDateTime.now();
        this.goldBalance = new BigDecimal("1000.000");
        this.reputation = 0;
    }

}
