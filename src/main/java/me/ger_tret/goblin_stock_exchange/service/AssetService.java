package me.ger_tret.goblin_stock_exchange.service;

import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.dto.AssetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    List<AssetResponseDto> getAllAssets();

    Page<AssetResponseDto> getAssetsPaged(Pageable pageable);

    AssetResponseDto getAssetDtoById(UUID id);

    Asset findAssetById(UUID id);

    AssetResponseDto getAssetByTicker(String ticker);
}