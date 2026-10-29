package com.groceryflow.inventory.controller;

import com.groceryflow.inventory.dto.*;
import com.groceryflow.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItemDto> getInventoryByProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryItemDto>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @PostMapping("/check")
    public ResponseEntity<StockCheckResponse> checkStock(@RequestBody StockCheckRequest request) {
        return ResponseEntity.ok(inventoryService.checkStock(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<StockReservationResponse> reserveStock(@RequestBody StockReservationRequest request) {
        return ResponseEntity.ok(inventoryService.reserveStock(request));
    }

    @PostMapping("/release")
    public ResponseEntity<StockReservationResponse> releaseStock(@RequestBody StockReservationRequest request) {
        return ResponseEntity.ok(inventoryService.releaseStock(request));
    }

    @PostMapping("/deduct")
    public ResponseEntity<StockReservationResponse> deductStock(@RequestBody StockReservationRequest request) {
        return ResponseEntity.ok(inventoryService.deductStock(request));
    }

    @PostMapping("/restock")
    public ResponseEntity<InventoryItemDto> restock(@Valid @RequestBody RestockRequest request) {
        return ResponseEntity.ok(inventoryService.restock(request));
    }
}
