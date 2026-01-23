package com.planadesk.backend.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private List<String> imageUrls;

    private int globalStock;   // same for all countries

    private List<CountryPrice> countryPrices;  // price per country

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public int getGlobalStock() { return globalStock; }
    public void setGlobalStock(int globalStock) { this.globalStock = globalStock; }

    public List<CountryPrice> getCountryPrices() { return countryPrices; }
    public void setCountryPrices(List<CountryPrice> countryPrices) {
        this.countryPrices = countryPrices;
    }
}
