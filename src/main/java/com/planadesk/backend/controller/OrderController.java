package com.planadesk.backend.controller;

import com.planadesk.backend.dto.CheckoutRequest;
import com.planadesk.backend.model.Order;
import com.planadesk.backend.repository.OrderRepository;
import com.planadesk.backend.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final OrderRepository orderRepository; // ✅ FIXED

    public OrderController(OrderService service, OrderRepository orderRepository) {
        this.service = service;
        this.orderRepository = orderRepository;
    }

    /* =====================
       CHECKOUT (UNCHANGED)
       ===================== */
    @PostMapping("/checkout")
    public Order checkout(
            @RequestBody CheckoutRequest req,
            Authentication auth
    ) {
        return service.placeOrder(
                auth.getName(),
                req.getAddress()
        );
    }

    /* =====================
       ADMIN: UPDATE STATUS (ADDED)
       ===================== */
    @PutMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable String id,
            @RequestParam String status
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    /* =====================
       USER ORDERS (UNCHANGED)
       ===================== */
    @GetMapping("/my")
    public List<Order> my(Authentication auth) {
        return service.getUserOrders(auth.getName());
    }

    /* =====================
       ADMIN ORDERS (UNCHANGED)
       ===================== */
    @GetMapping
    public List<Order> all() {
        return service.getAllOrders();
    }
}