package com.planadesk.backend.model;

public class CountryPrice {

    private String countryCode;  // must match from Country collection
    private double price;

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
