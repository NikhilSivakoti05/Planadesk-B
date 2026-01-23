package com.planadesk.backend.dto;

import com.planadesk.backend.model.Address;

public class CheckoutRequest {
    private Address address;

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}
