package com.it120p.librarymanagementsystem.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The UserDetailsService interface is a part of Spring Security framework.
 *
 * It is used to retrieve user-related data and is used by the security framework to handle data in the authentication process.
 * The primary purpose of this interface is to load the user for further authentication.
 * It works by checking the user's existence in your application's storage area (database, LDAP, memory, etc.).
 *
 * The loadUserByUsername method is the only method defined in the interface, and the implementation of this method is used in the authentication process.
 */
public interface UserDetailsService {
    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly be case sensitive, or case insensitive depending on how it's implemented in the interface.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never null).
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority.
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
