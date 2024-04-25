package com.it120p.librarymanagementsystem.security.services;

import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserDetailsServiceImpl class implements the UserDetailsService interface from Spring Security.
 *
 * This class is marked with the @Service annotation, meaning that it is a candidate for Spring's component scanning to detect and add to the application context.
 *
 * The UserRepository is autowired and used to fetch user data from the database.
 *
 * The loadUserByUsername method is overridden to provide custom authentication logic. It fetches the user from the database using the username. If the user is not found, it throws a UsernameNotFoundException. If the user is found, it builds and returns a UserDetailsImpl object from the User object.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly be case sensitive, or case insensitive depending on how it's implemented in the interface.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never null).
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
