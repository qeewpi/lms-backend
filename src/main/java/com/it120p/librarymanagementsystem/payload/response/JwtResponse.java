package com.it120p.librarymanagementsystem.payload.response;

import java.util.List;

/**
 * The JwtResponse class is a model for the data returned after a successful authentication.
 * It contains fields for the JWT token, token type, user's ID, username, name, email, and roles.
 * It is necessary to return the JWT token to the client so that it can be stored and used for future requests.
 *
 * The token is the JWT token used for authentication.
 * The type is the type of the token, which is "Bearer".
 * The id is the unique identifier of the authenticated user.
 * The username is the username of the authenticated user.
 * The name is the name of the authenticated user.
 * The email is the email of the authenticated user.
 * The roles are the roles of the authenticated user.
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String name;
    private String email;
    private List<String> roles;

    /**
     * The JwtResponse class is a model for the data returned after a successful authentication.
     * It contains fields for the JWT token, token type, user's ID, username, name, email, and roles.
     *
     * The token is the JWT token used for authentication.
     * The type is the type of the token, which is "Bearer".
     * The id is the unique identifier of the authenticated user.
     * The username is the username of the authenticated user.
     * The name is the name of the authenticated user.
     * The email is the email of the authenticated user.
     * The roles are the roles of the authenticated user.
     */
    public JwtResponse(String accessToken, Long id, String username, String name, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}