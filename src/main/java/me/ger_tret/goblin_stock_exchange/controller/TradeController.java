package me.ger_tret.goblin_stock_exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.dto.TradeTransactionDto;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.TradeTransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
@Tag(name = "Market History", description = "Public tape of executed trades")
public class TradeController {

    private final TradeTransactionRepository transactionRepository;
    private final EntityMapper mapper;

    @Operation(summary = "Get recent market activity")
    @GetMapping("/recent")
    public ResponseEntity<List<TradeTransactionDto>> getRecentTrades() {
        return ResponseEntity.ok(
                transactionRepository.findTop50ByOrderByExecutedAtDesc().stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }
}
