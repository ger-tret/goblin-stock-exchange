package me.ger_tret.goblin_stock_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.dto.BrokerDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.InventoryResponseDto;
import me.ger_tret.goblin_stock_exchange.service.BrokerService;
import me.ger_tret.goblin_stock_exchange.service.InventoryService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/brokers")
@RequiredArgsConstructor
@Tag(name = "Broker Management", description = "Endpoints for goblin profiles, gold balances, and inventories")
public class BrokerController {

    private final BrokerService brokerService;
    private final InventoryService inventoryService;

    @Operation(summary = "Get broker profile")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BrokerDto>> getBrokerById(@PathVariable UUID id) {
        BrokerDto broker = brokerService.getBrokerDtoById(id);

        return ResponseEntity.ok(
                EntityModel.of(broker,
                        linkTo(methodOn(BrokerController.class).getBrokerById(id)).withSelfRel(),
                        linkTo(methodOn(BrokerController.class).getBrokerInventory(id)).withRel("inventory"),
                        linkTo(methodOn(OrderController.class).getBrokerOrders(id)).withRel("orders")
                )
        );

    }

    @Operation(summary = "View broker's backpack (inventory)")
    @GetMapping("/{id}/inventory")
    public ResponseEntity<CollectionModel<EntityModel<InventoryResponseDto>>> getBrokerInventory(@PathVariable UUID id) {
        List<InventoryResponseDto> inventory = inventoryService.getBrokerInventory(id);

        List<EntityModel<InventoryResponseDto>> inventoryModels = inventory.stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(AssetController.class).getAssetById(item.assetId())).withRel("asset_details")))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(inventoryModels,
                        linkTo(methodOn(BrokerController.class).getBrokerInventory(id)).withSelfRel(),
                        linkTo(methodOn(BrokerController.class).getBrokerById(id)).withRel("broker_profile"))
        );
    }


    @Operation(summary = "Register a new human trader")
    @PostMapping("/register")
    public ResponseEntity<EntityModel<BrokerDto>> register(@RequestParam String username) {
        BrokerDto newBroker = brokerService.registerBroker(username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EntityModel.of(newBroker,
                        linkTo(methodOn(BrokerController.class).getBrokerById(newBroker.id())).withSelfRel()));
    }


    @Operation(summary = "Deposit gold (simulation of wealth)")
    @PostMapping("/{id}/top-up")
    public ResponseEntity<Void> topUpBalance(
            @PathVariable UUID id,
            @RequestParam @Positive BigDecimal amount) {
        brokerService.updateBalance(id, amount);
        return ResponseEntity.noContent().build();
    }

}

