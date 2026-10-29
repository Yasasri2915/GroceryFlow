package com.groceryflow.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RestockRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Restock quantity must be at least 1")
    private Integer quantity;

    private String notes;

    public RestockRequest() {
    }

    public RestockRequest(Long productId, Integer quantity, String notes) {
        this.productId = productId;
        this.quantity = quantity;
        this.notes = notes;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
