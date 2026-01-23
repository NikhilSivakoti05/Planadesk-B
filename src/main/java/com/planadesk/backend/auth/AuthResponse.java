package com.planadesk.backend.auth;

public class AuthResponse {
    private String token;
    private String role;
    private String firstName;
    private String lastName;

    public AuthResponse(String token, String role, String firstName, String lastName) {
        this.token = token;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
