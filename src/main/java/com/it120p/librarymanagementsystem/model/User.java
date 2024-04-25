package com.it120p.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The User class is an entity model object for a User.
 * It contains fields for the user's ID, username, name, email, password, roles, and orders.
 * The ID is generated automatically when a User entity is created.
 * The username and email are unique for each User entity.
 */
@Entity
@Table(name = "users",
    // Unique constraints for username and email to ensure they are unique
    uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size (max=20)
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    @Size (max=50)
    @Email
    private String email;

    @NotBlank
    @Size (max=120)
    private String password;

    /**
     * Many-to-Many relationship between User and Role entities.
     * A User can have multiple roles.
     * FetchType.LAZY is used to load the roles only when needed.
     * The join table is user_roles, with user_id and role_id as the foreign keys.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            // Foreign key for User entity in user_roles table
            joinColumns = @JoinColumn(name = "user_id"),
            // Foreign key for Role entity in user_roles table
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * One-to-Many relationship between User and Order entities.
     * A User can have multiple orders.
     * FetchType.LAZY is used to load the orders only when needed.
     * The foreign key is user_id in the orders table.
     * The user field in the Order entity is the owning side of the relationship.
     * The JsonManagedReference annotation is used to prevent infinite recursion when serializing the object to JSON.
     */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Order> orders;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User() {
    }

    /**
     * Constructor for the User entity with username, name, email, and password parameters.
     *
     * @param username the username of the user.
     * @param name the name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     */
    public User(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
