package me.ger_tret.goblin_stock_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.ger_tret.goblin_stock_exchange.entity.dto.AssetResponseDto;
import me.ger_tret.goblin_stock_exchange.service.AssetService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Market Assets", description = "Endpoints for exploring goblin-traded commodites and runes")
@Validated
public class AssetController {
    private final AssetService assetService;


    @Operation(summary = "Browse the market", description = "Returns a paged list of all available assets in the exchange.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of assets")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AssetResponseDto>>> getAllAssets(
            @ParameterObject Pageable pageable,
            PagedResourcesAssembler<AssetResponseDto> assembler) {

        Page<AssetResponseDto> assetsPage = assetService.getAssetsPaged(pageable);

        PagedModel<EntityModel<AssetResponseDto>> pagedModel = assembler.toModel(assetsPage, asset ->
                EntityModel.of(asset,
                        linkTo(methodOn(AssetController.class).assetService.getAssetDtoById(asset.id())).withSelfRel(),
                        linkTo(methodOn(AssetController.class).assetService.getAssetByTicker(asset.ticker())).withRel("details_by_ticker")
                )
        );

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get asset by UUID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AssetResponseDto>> getAssetById(@PathVariable UUID id) {
        AssetResponseDto asset = assetService.getAssetDtoById(id);

        return ResponseEntity.ok(
                EntityModel.of(asset,
                        linkTo(methodOn(AssetController.class).getAssetById(id)).withSelfRel(),
                        linkTo(methodOn(AssetController.class).getAllAssets(PageRequest.of(0, 20), null)).withRel("all_assets")
                )
        );
    }

    @Operation(summary = "Get asset by its unique ticker (e.g., SLM, NSFT)")
    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<EntityModel<AssetResponseDto>> getAssetByTicker(@PathVariable String ticker) {
        AssetResponseDto asset = assetService.getAssetByTicker(ticker);

        return ResponseEntity.ok(
                EntityModel.of(asset,
                        linkTo(methodOn(AssetController.class).getAssetByTicker(ticker)).withSelfRel(),
                        linkTo(methodOn(AssetController.class).getAssetById(asset.id())).withRel("canonical_link")
                )
        );

    }


}
