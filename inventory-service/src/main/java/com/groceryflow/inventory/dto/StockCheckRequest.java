package com.groceryflow.inventory.dto;

import java.util.List;

public class StockCheckRequest {

    private List<ProductStockDto> items;

    public StockCheckRequest() {
    }

    public StockCheckRequest(List<ProductStockDto> items) {
        this.items = items;
    }

    public List<ProductStockDto> getItems() {
        return items;
    }

    public void setItems(List<ProductStockDto> items) {
        this.items = items;
    }
}
