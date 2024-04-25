package com.it120p.librarymanagementsystem.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * The LoginRequest class is a model for the data required for a login request.
 * It is used to map the JSON object from the HTTP request to a Java object,
 * which is used to authenticate the user.
 * It contains fields for the username and password.
 * Both fields are mandatory for a successful login request.
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}