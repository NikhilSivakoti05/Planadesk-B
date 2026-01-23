package com.planadesk.backend.service;

import com.planadesk.backend.model.*;
import com.planadesk.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartService cartService;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderService(
            CartService cartService,
            ProductRepository productRepo,
            OrderRepository orderRepo
    ) {
        this.cartService = cartService;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    /* =========================
       PLACE ORDER
       ========================= */
    public Order placeOrder(String email, Address address) {

        Cart cart = cartService.getCart(email);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart empty");
        }

        double total = 0;

        for (CartItem item : cart.getItems()) {
            Product p = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (p.getGlobalStock() < item.getQuantity()) {
                throw new RuntimeException("Out of stock");
            }

            // ✅ reduce stock
            p.setGlobalStock(p.getGlobalStock() - item.getQuantity());
            productRepo.save(p);

            total += item.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUserEmail(email);
        order.setAddress(address);
        order.setItems(
            cart.getItems().stream().map(i -> {
                OrderItem oi = new OrderItem();
                oi.setProductId(i.getProductId());
                oi.setName(i.getName());
                oi.setCountry(i.getCountry());
                oi.setPrice(i.getPrice());
                oi.setQuantity(i.getQuantity());
                return oi;
            }).toList()
        );

        order.setTotalAmount(total);
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        // ✅ clear cart after order
        cartService.clear(email);

        return orderRepo.save(order);
    }

    /* =========================
       USER ORDERS
       ========================= */
    public List<Order> getUserOrders(String email) {
        return orderRepo.findByUserEmailOrderByOrderDateDesc(email);
    }

    /* =========================
       ADMIN – ALL ORDERS
       ========================= */
    public List<Order> getAllOrders() {
        return orderRepo.findAllByOrderByOrderDateDesc();
    }
}
