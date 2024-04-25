package com.it120p.librarymanagementsystem.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.it120p.librarymanagementsystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The UserDetailsImpl class implements the UserDetails interface from Spring Security.
 *
 * This class is used to provide core user information to the framework, which is later encapsulated into Authentication objects.
 * This allows non-security related user information (such as email addresses, telephone numbers etc) to be stored.
 *
 * The UserDetailsImpl class includes details like username, password, and granted authorities.
 * The build method is a static method that constructs and returns a UserDetailsImpl object from a User object.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    private String name;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a UserDetailsImpl object with the provided parameters.
     *
     * @param id the ID of the user.
     * @param username the username of the user.
     * @param name the name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @param authorities the authorities granted to the user.
     */
    public UserDetailsImpl(Long id, String username, String name, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Constructs and returns a UserDetailsImpl object from a User object.
     *
     * @param user the User object from which to construct the UserDetailsImpl object.
     * @return a UserDetailsImpl object.
     */
    public static UserDetailsImpl build(User user) {
        // Used stream to iterate over the roles of the user and map them to a list of authorities.
        List<GrantedAuthority> authorities = user.getRoles().stream()
                // For each role, map it to a SimpleGrantedAuthority object with the name of the role.
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // Return a new UserDetailsImpl object with the user's ID, username, name, email, password, and authorities.
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
