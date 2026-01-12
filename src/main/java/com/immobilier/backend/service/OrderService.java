// src/main/java/com/ecommerce/backend/service/OrderService.java
package com.immobilier.backend.service;

import com.immobilier.backend.entity.Order;
import com.immobilier.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired; // Keep this import, but it's not strictly necessary if only using constructor injection
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // @Autowired
    // private OrderService orderService; 

    // ✅ This is correctly injected via the constructor below.
    private final OrderRepository orderRepository;

    // ✅ Constructor Injection is the preferred method in modern Spring.
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Existing methods...

    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ... Any other existing methods (like find, create, etc.) should be here ...
}