package com.planadesk.backend.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private String id;

    // ✅ who placed order
    private String userEmail;

    // ✅ full address snapshot
    private Address address;

    // ✅ full cart items
    private List<OrderItem> items;

    private double totalAmount;
    private String status;
    private LocalDateTime orderDate;

    // getters & setters
 // Getters
    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Address getAddress() {
        return address;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
