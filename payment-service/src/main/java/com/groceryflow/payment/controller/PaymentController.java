package com.groceryflow.payment.controller;

import com.groceryflow.payment.dto.PaymentResponseDto;
import com.groceryflow.payment.dto.ProcessPaymentRequest;
import com.groceryflow.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
