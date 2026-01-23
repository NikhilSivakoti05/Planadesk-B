package com.planadesk.backend.service;

import com.planadesk.backend.model.*;
import com.planadesk.backend.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CartService {

    private final CartRepository repo;

    public CartService(CartRepository repo) {
        this.repo = repo;
    }

    public Cart getCart(String email) {
        return repo.findByUserEmail(email)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserEmail(email);
                    c.setItems(new ArrayList<>());
                    return c;
                });
    }
    public void clear(String email) {
        repo.deleteByUserEmail(email);
    }

    public Cart add(String email, CartItem item) {
        Cart cart = getCart(email);

        // ✅ single country rule
        if (cart.getCountry() != null &&
            !cart.getCountry().equals(item.getCountry())) {
            throw new RuntimeException("Only one country allowed per cart");
        }

        cart.setCountry(item.getCountry());

        cart.getItems().stream()
            .filter(i -> i.getProductId().equals(item.getProductId()))
            .findFirst()
            .ifPresentOrElse(
                i -> i.setQuantity(i.getQuantity() + item.getQuantity()),
                () -> cart.getItems().add(item)
            );

        cart.setUpdatedAt(LocalDateTime.now());
        return repo.save(cart);
    }

    public Cart updateQty(String email, String productId, int qty) {
        Cart cart = getCart(email);
        cart.getItems().forEach(i -> {
            if (i.getProductId().equals(productId)) {
                i.setQuantity(qty);
            }
        });
        return repo.save(cart);
    }

    public Cart remove(String email, String productId) {
        Cart cart = getCart(email);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        if (cart.getItems().isEmpty()) {
            cart.setCountry(null);
        }

        return repo.save(cart);
    }

   
}
