package me.ger_tret.goblin_stock_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.Order;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderRequestDto;
import me.ger_tret.goblin_stock_exchange.entity.dto.OrderResponseDto;
import me.ger_tret.goblin_stock_exchange.entity.enums.OrderStatus;
import me.ger_tret.goblin_stock_exchange.exception.GseException;
import me.ger_tret.goblin_stock_exchange.mapper.EntityMapper;
import me.ger_tret.goblin_stock_exchange.repository.OrderRepository;
import me.ger_tret.goblin_stock_exchange.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Trading Operations", description = "Place and cancel orders in the exchange")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final EntityMapper mapper;


    @Operation(summary = "Place a new order", description = "Submit a buy or sell request. Requires sufficient gold or assets.")
    @PostMapping("/broker/{brokerId}")
    public ResponseEntity<EntityModel<OrderResponseDto>> placeOrder(
            @PathVariable UUID brokerId,
            @Valid @RequestBody OrderRequestDto orderRequestDto
            ) {
        UUID orderId = orderService.placeOrder(brokerId, orderRequestDto);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GseException("Order creation failed - not found"));

        OrderResponseDto responseDto = mapper.toOrderResponseDto(order);

        EntityModel<OrderResponseDto> model = EntityModel.of(responseDto,
                linkTo(methodOn(OrderController.class).getBrokerOrders(brokerId)).withRel("broker_orders"),
                linkTo(methodOn(AssetController.class).getAssetById(responseDto.assetId())).withRel("asset_details"),
                linkTo(methodOn(OrderController.class).cancelOrder(orderId)).withRel("cancel"));

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getBrokerOrders(brokerId)).toUri())
                .body(model);
    }

    @Operation(summary = "Cancel an open order")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get all orders for a specific broker")
    @GetMapping("/broker/{brokerId}")
    public ResponseEntity<CollectionModel<EntityModel<OrderResponseDto>>> getBrokerOrders(@PathVariable UUID brokerId) {
        List<OrderResponseDto> orders = orderService.getOrdersByBroker(brokerId);

        List<EntityModel<OrderResponseDto>> orderModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(AssetController.class).getAssetById(order.assetId())).withRel("asset"),
                        order.status() == OrderStatus.OPEN ?
                                linkTo(methodOn(OrderController.class).cancelOrder(order.id())).withRel("cancel") : null
                ))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(orderModels,
                        linkTo(methodOn(OrderController.class).getBrokerOrders(brokerId)).withSelfRel())
        );
    }
}
