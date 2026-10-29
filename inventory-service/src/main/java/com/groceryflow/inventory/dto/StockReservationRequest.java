package com.groceryflow.inventory.dto;

import java.util.List;

public class StockReservationRequest {

    private Long orderId;
    private List<ProductStockDto> items;

    public StockReservationRequest() {
    }

    public StockReservationRequest(Long orderId, List<ProductStockDto> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<ProductStockDto> getItems() {
        return items;
    }

    public void setItems(List<ProductStockDto> items) {
        this.items = items;
    }
}
