package me.ger_tret.goblin_stock_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.Asset;
import me.ger_tret.goblin_stock_exchange.entity.dto.AssetResponseDto;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.AssetRepository;
import me.ger_tret.goblin_stock_exchange.service.AssetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final EntityMapper mapper;

    @Override
    public List<AssetResponseDto> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Page<AssetResponseDto> getAssetsPaged(Pageable pageable) {
        return assetRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public AssetResponseDto getAssetDtoById(UUID id) {
        return assetRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GseException("Asset not found"));
    }

    @Override
    public Asset findAssetById(UUID id) {
        return assetRepository.findById(id).
                orElseThrow(() -> new GseException("Asset not found"));
    }

    @Override
    public AssetResponseDto getAssetByTicker(String ticker) {
        return assetRepository.findByTicker(ticker)
                .map(mapper::toDto)
                .orElseThrow(() -> new GseException("Asset not found"));
    }
}