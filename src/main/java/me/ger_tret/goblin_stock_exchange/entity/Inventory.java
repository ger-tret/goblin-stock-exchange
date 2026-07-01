package me.ger_tret.goblin_stock_exchange.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Broker broker;

    @ManyToOne(fetch = FetchType.LAZY)
    private Asset asset;

    private Integer quantity;
}
