package com.groceryflow.inventory.dto;

import com.groceryflow.inventory.entity.InventoryItem;

import java.time.LocalDateTime;

public class InventoryItemDto {

    private Long id;
    private Long productId;
    private int availableQuantity;
    private int reservedQuantity;
    private int lowStockThreshold;
    private boolean lowStock;
    private LocalDateTime updatedAt;

    public InventoryItemDto() {
    }

    public static InventoryItemDto fromEntity(InventoryItem item) {
        InventoryItemDto dto = new InventoryItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setAvailableQuantity(item.getAvailableQuantity());
        dto.setReservedQuantity(item.getReservedQuantity());
        dto.setLowStockThreshold(item.getLowStockThreshold());
        dto.setLowStock(item.isLowStock());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
