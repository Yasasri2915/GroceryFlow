package com.groceryflow.inventory.dto;

import java.util.List;

public class StockCheckResponse {

    private boolean available;
    private List<Long> unavailableProductIds;
    private String message;

    public StockCheckResponse() {
    }

    public StockCheckResponse(boolean available, List<Long> unavailableProductIds, String message) {
        this.available = available;
        this.unavailableProductIds = unavailableProductIds;
        this.message = message;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Long> getUnavailableProductIds() {
        return unavailableProductIds;
    }

    public void setUnavailableProductIds(List<Long> unavailableProductIds) {
        this.unavailableProductIds = unavailableProductIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
