package com.planadesk.backend.controller;

import com.planadesk.backend.dto.CartRequest;
import com.planadesk.backend.model.Cart;
import com.planadesk.backend.model.CartItem;
import com.planadesk.backend.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    /* =====================
       ADD TO CART
       ===================== */
    @PostMapping("/add")
    public Cart add(@RequestBody CartRequest req, Authentication auth) {

        CartItem item = new CartItem();
        item.setProductId(req.getProductId());
        item.setName(req.getName());
        item.setCountry(req.getCountry());
        item.setPrice(req.getPrice());
        item.setQuantity(req.getQuantity());
        item.setImage(req.getImage());

        // ✅ MATCHES CartService.add(...)
        return service.add(auth.getName(), item);
    }

    /* =====================
       GET CART
       ===================== */
    @GetMapping
    public Cart get(Authentication auth) {
        return service.getCart(auth.getName());
    }

    /* =====================
       UPDATE QUANTITY
       ===================== */
    @PutMapping("/update")
    public Cart update(
            @RequestBody CartRequest req,
            Authentication auth
    ) {
        // ✅ MATCHES CartService.updateQty(...)
        return service.updateQty(
                auth.getName(),
                req.getProductId(),
                req.getQuantity()
        );
    }

    /* =====================
       REMOVE ITEM
       ===================== */
    @DeleteMapping("/remove")
    public Cart remove(
            @RequestBody CartRequest req,
            Authentication auth
    ) {
        // ✅ MATCHES CartService.remove(...)
        return service.remove(
                auth.getName(),
                req.getProductId()
        );
    }
}
