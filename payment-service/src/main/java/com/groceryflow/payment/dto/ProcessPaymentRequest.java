package com.groceryflow.payment.dto;

import com.groceryflow.payment.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProcessPaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Optional flag to simulate failure for testing/demonstrating rollback compensation!
    private boolean simulateFailure = false;

    public ProcessPaymentRequest() {
    }

    public ProcessPaymentRequest(Long orderId, Long customerId, BigDecimal amount, PaymentMethod paymentMethod, boolean simulateFailure) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.simulateFailure = simulateFailure;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isSimulateFailure() {
        return simulateFailure;
    }

    public void setSimulateFailure(boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }
}
