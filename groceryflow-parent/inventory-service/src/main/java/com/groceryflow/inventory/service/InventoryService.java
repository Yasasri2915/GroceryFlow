package com.groceryflow.inventory.service;

import com.groceryflow.inventory.dto.*;
import com.groceryflow.inventory.entity.InventoryItem;
import com.groceryflow.inventory.entity.InventoryTransaction;
import com.groceryflow.inventory.entity.TransactionType;
import com.groceryflow.inventory.exception.InsufficientStockException;
import com.groceryflow.inventory.repository.InventoryRepository;
import com.groceryflow.inventory.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final TransactionRepository transactionRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            TransactionRepository transactionRepository) {
        this.inventoryRepository = inventoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(InventoryItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventoryItemDto getInventoryByProductId(Long productId) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> inventoryRepository.save(new InventoryItem(productId, 0, 10)));
        return InventoryItemDto.fromEntity(item);
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDto> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(InventoryItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StockCheckResponse checkStock(StockCheckRequest request) {
        List<Long> unavailableIds = new ArrayList<>();

        for (ProductStockDto itemReq : request.getItems()) {
            InventoryItem item = inventoryRepository.findByProductId(itemReq.getProductId()).orElse(null);
            if (item == null || item.getAvailableQuantity() < itemReq.getQuantity()) {
                unavailableIds.add(itemReq.getProductId());
            }
        }

        if (unavailableIds.isEmpty()) {
            return new StockCheckResponse(true, List.of(), "All requested items are in stock");
        } else {
            return new StockCheckResponse(false, unavailableIds, "Some items are out of stock or exceed available quantity");
        }
    }

    @Transactional
    public StockReservationResponse reserveStock(StockReservationRequest request) {
        // 1. Verify availability of ALL items first (atomic check)
        for (ProductStockDto itemReq : request.getItems()) {
            InventoryItem item = inventoryRepository.findByProductId(itemReq.getProductId())
                    .orElseThrow(() -> new InsufficientStockException("Product " + itemReq.getProductId() + " not found in inventory"));

            if (item.getAvailableQuantity() < itemReq.getQuantity()) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for Product ID %d. Requested: %d, Available: %d",
                                itemReq.getProductId(), itemReq.getQuantity(), item.getAvailableQuantity()));
            }
        }

        // 2. Perform reservations
        for (ProductStockDto itemReq : request.getItems()) {
            InventoryItem item = inventoryRepository.findByProductId(itemReq.getProductId()).get();
            item.setAvailableQuantity(item.getAvailableQuantity() - itemReq.getQuantity());
            item.setReservedQuantity(item.getReservedQuantity() + itemReq.getQuantity());
            inventoryRepository.save(item);

            transactionRepository.save(new InventoryTransaction(
                    item.getProductId(),
                    TransactionType.RESERVE,
                    itemReq.getQuantity(),
                    request.getOrderId(),
                    "Reserved for order #" + request.getOrderId()
            ));
        }

        return new StockReservationResponse(true, request.getOrderId(), "Stock reserved successfully");
    }

    @Transactional
    public StockReservationResponse releaseStock(StockReservationRequest request) {
        for (ProductStockDto itemReq : request.getItems()) {
            InventoryItem item = inventoryRepository.findByProductId(itemReq.getProductId()).orElse(null);
            if (item != null) {
                int releaseQty = Math.min(item.getReservedQuantity(), itemReq.getQuantity());
                item.setReservedQuantity(item.getReservedQuantity() - releaseQty);
                item.setAvailableQuantity(item.getAvailableQuantity() + releaseQty);
                inventoryRepository.save(item);

                transactionRepository.save(new InventoryTransaction(
                        item.getProductId(),
                        TransactionType.RELEASE,
                        releaseQty,
                        request.getOrderId(),
                        "Released from order #" + request.getOrderId()
                ));
            }
        }
        return new StockReservationResponse(true, request.getOrderId(), "Stock released successfully");
    }

    @Transactional
    public StockReservationResponse deductStock(StockReservationRequest request) {
        for (ProductStockDto itemReq : request.getItems()) {
            InventoryItem item = inventoryRepository.findByProductId(itemReq.getProductId()).orElse(null);
            if (item != null) {
                int deductQty = Math.min(item.getReservedQuantity(), itemReq.getQuantity());
                item.setReservedQuantity(item.getReservedQuantity() - deductQty);
                inventoryRepository.save(item);

                transactionRepository.save(new InventoryTransaction(
                        item.getProductId(),
                        TransactionType.DEDUCT,
                        deductQty,
                        request.getOrderId(),
                        "Deducted for fulfilled order #" + request.getOrderId()
                ));
            }
        }
        return new StockReservationResponse(true, request.getOrderId(), "Stock deducted successfully");
    }

    @Transactional
    public InventoryItemDto restock(RestockRequest request) {
        InventoryItem item = inventoryRepository.findByProductId(request.getProductId())
                .orElseGet(() -> new InventoryItem(request.getProductId(), 0, 10));

        item.setAvailableQuantity(item.getAvailableQuantity() + request.getQuantity());
        InventoryItem saved = inventoryRepository.save(item);

        transactionRepository.save(new InventoryTransaction(
                saved.getProductId(),
                TransactionType.RESTOCK,
                request.getQuantity(),
                null,
                request.getNotes() != null ? request.getNotes() : "Manual inventory restock"
        ));

        return InventoryItemDto.fromEntity(saved);
    }
}
