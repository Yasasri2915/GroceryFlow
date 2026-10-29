package com.groceryflow.order.service;

import com.groceryflow.order.dto.CreateOrderRequest;
import com.groceryflow.order.entity.*;
import com.groceryflow.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();

        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomerId(request.getCustomerId() != null ? request.getCustomerId() : 1L);
        order.setCustomerName(request.getCustomerName() != null ? request.getCustomerName() : "Customer");
        order.setCustomerEmail(request.getCustomerEmail() != null ? request.getCustomerEmail() : "customer@example.com");
        order.setCustomerPhone(request.getCustomerPhone() != null ? request.getCustomerPhone() : "9876543210");
        order.setDeliveryAddress(request.getDeliveryAddress() != null ? request.getDeliveryAddress() : "Standard Delivery Address");

        order.setStatus(OrderStatus.PLACED);
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Safe resolution for CREDIT_CARD, CASH, or UPI
        PaymentMethod resolvedPaymentMethod = PaymentMethod.CREDIT_CARD;
        if (request.getPaymentMethod() != null) {
            try {
                resolvedPaymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                resolvedPaymentMethod = PaymentMethod.CREDIT_CARD;
            }
        }
        order.setPaymentMethod(resolvedPaymentMethod);

        BigDecimal subtotal = BigDecimal.ZERO;
        order.setItems(new ArrayList<>());

        if (request.getItems() != null) {
            for (var itemReq : request.getItems()) {
                OrderItem item = new OrderItem();
                item.setProductId(itemReq.getProductId());
                item.setQuantity(itemReq.getQuantity());
                item.setProductName("Product #" + itemReq.getProductId());

                BigDecimal price = new BigDecimal(String.valueOf(itemReq.getPrice()));
                item.setUnitPrice(price);

                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
                item.setTotalPrice(itemTotal);

                order.addItem(item);
                subtotal = subtotal.add(itemTotal);
            }
        }

        order.setSubtotal(subtotal != null ? subtotal : BigDecimal.ZERO);
        BigDecimal deliveryFee = new BigDecimal("5.00");
        order.setDeliveryFee(deliveryFee);
        order.setTotalAmount(subtotal.add(deliveryFee));

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}