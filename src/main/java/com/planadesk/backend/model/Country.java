package com.planadesk.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "countries")
public class Country {

    @Id
    private String id;

    private String countryCode;   // IN, US, AE
    private String countryName;   // India, USA, UAE
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
