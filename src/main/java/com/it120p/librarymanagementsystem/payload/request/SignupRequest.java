package com.it120p.librarymanagementsystem.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import jakarta.validation.constraints.*;

/**
 * The SignupRequest class is a model for the data required for a signup request.
 * It is used to map the JSON object from the frontend to a Java object.
 * It is needed to create a new user account.
 * It contains fields for the username, name, email, role, and password.
 * All fields are mandatory for a successful signup request.
 * The username must be between 3 and 20 characters.
 * The email must be a valid email format.
 * The password must be between 6 and 40 characters.
 */
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}