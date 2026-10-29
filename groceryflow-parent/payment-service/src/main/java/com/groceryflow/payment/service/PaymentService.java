package com.groceryflow.payment.service;

import com.groceryflow.payment.dto.PaymentResponseDto;
import com.groceryflow.payment.dto.ProcessPaymentRequest;
import com.groceryflow.payment.entity.Payment;
import com.groceryflow.payment.entity.PaymentStatus;
import com.groceryflow.payment.exception.PaymentFailedException;
import com.groceryflow.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponseDto processPayment(ProcessPaymentRequest request) {
        String txnRef = "TXN-" + UUID.randomUUID().toString().substring(0, 13).toUpperCase();

        if (request.isSimulateFailure()) {
            Payment failedPayment = new Payment(
                    request.getOrderId(),
                    request.getCustomerId(),
                    request.getAmount(),
                    request.getPaymentMethod(),
                    PaymentStatus.FAILED,
                    txnRef,
                    "{\"error\":\"Card declined / simulated payment failure\"}"
            );
            paymentRepository.save(failedPayment);
            throw new PaymentFailedException("Payment processing failed: Transaction declined by bank for reference " + txnRef);
        }

        Payment successfulPayment = new Payment(
                request.getOrderId(),
                request.getCustomerId(),
                request.getAmount(),
                request.getPaymentMethod(),
                PaymentStatus.SUCCESS,
                txnRef,
                "{\"status\":\"APPROVED\",\"gateway\":\"MockPaymentEngine-v1\"}"
        );

        Payment saved = paymentRepository.save(successfulPayment);
        return PaymentResponseDto.fromEntity(saved, true, "Payment processed successfully");
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentFailedException("No payment record found for order ID: " + orderId));
        return PaymentResponseDto.fromEntity(payment, payment.getStatus() == PaymentStatus.SUCCESS, "Payment record retrieved");
    }
}
