package com.groceryflow.inventory.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends RuntimeException {

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public InsufficientStockException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
