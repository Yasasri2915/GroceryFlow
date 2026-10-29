package com.groceryflow.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends RuntimeException {

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public PaymentFailedException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
