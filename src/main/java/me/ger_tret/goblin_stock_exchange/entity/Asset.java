package me.ger_tret.goblin_stock_exchange.entity;


import jakarta.persistence.*;
import lombok.*;
import me.ger_tret.goblin_stock_exchange.entity.constant.AssetType;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, length = 10)
    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type")
    private AssetType type;


    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    private String description;
}
