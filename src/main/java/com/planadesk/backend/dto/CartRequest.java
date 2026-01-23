package com.planadesk.backend.dto;

public class CartRequest {

    private String productId;
    private String name;
    private String country;
    private double price;
    private int quantity;
    private String image;

    // getters & setters
 // Getters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    // Setters
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
