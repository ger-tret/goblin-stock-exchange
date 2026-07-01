package me.ger_tret.goblin_stock_exchange.mapper;

import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.Broker;
import me.ger_tret.goblin_stock_exchange.entity.dto.AssetResponseDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    BrokerDto toDto(Broker broker);

    @Mapping(target = "currentPrice", source = "basePrice")
    AssetResponseDto toDto(Asset asset);
}
