package com.planadesk.backend.controller;

import com.planadesk.backend.dto.CheckoutRequest;
import com.planadesk.backend.model.Order;
import com.planadesk.backend.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /* =====================
       CHECKOUT
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
       USER ORDERS
       ===================== */
    @GetMapping("/my")
    public List<Order> my(Authentication auth) {
        return service.getUserOrders(auth.getName());
    }

    /* =====================
       ADMIN ORDERS
       ===================== */
    @GetMapping
    public List<Order> all() {
        return service.getAllOrders();
    }
}
