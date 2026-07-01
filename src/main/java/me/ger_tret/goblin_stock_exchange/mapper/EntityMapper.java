package me.ger_tret.goblin_stock_exchange.mapper;

import me.ger_tret.goblin_stock_exchange.entity.*;
import me.ger_tret.goblin_stock_exchange.entity.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    BrokerDto toDto(Broker broker);

    @Mapping(target = "currentPrice", source = "basePrice")
    AssetResponseDto toDto(Asset asset);

    @Mapping(target = "assetId", source = "asset.id")
    @Mapping(target = "assetName", source = "asset.name")
    @Mapping(target = "type", source = "orderType")
    OrderResponseDto toOrderResponseDto(Order order);


    @Mapping(target = "assetId", source = "asset.id")
    @Mapping(target = "assetName", source = "asset.name")
    @Mapping(target = "ticker", source = "asset.ticker")
    InventoryResponseDto toDto(Inventory inventory);

    @Mapping(target = "buyerName", source = "buyOrder.broker.username")
    @Mapping(target = "sellerName", source = "sellOrder.broker.username")
    @Mapping(target = "assetTicker", source = "buyOrder.asset.ticker")
    TradeTransactionDto toDto(TradeTransaction transaction);

}
