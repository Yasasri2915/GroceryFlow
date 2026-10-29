package com.groceryflow.order.controller;

import com.groceryflow.order.dto.CreateOrderRequest;
import com.groceryflow.order.entity.Order;
import com.groceryflow.order.entity.OrderStatus;
import com.groceryflow.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order createdOrder = orderService.createOrder(request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {
        Order updatedOrder = orderService.updateOrderStatus(id, OrderStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(updatedOrder);
    }
}