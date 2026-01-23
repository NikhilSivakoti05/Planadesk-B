package com.planadesk.backend.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cart {
    private String id;
    private String userEmail;
    private String country; // ✅ SINGLE COUNTRY ENFORCED
    private List<CartItem> items;
    private LocalDateTime updatedAt;

    // getters & setters
 // Getters
    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCountry() {
        return country;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

