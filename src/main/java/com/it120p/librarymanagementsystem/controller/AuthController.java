package com.it120p.librarymanagementsystem.controller;


import com.it120p.librarymanagementsystem.model.ERole;
import com.it120p.librarymanagementsystem.model.Role;
import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.payload.request.LoginRequest;
import com.it120p.librarymanagementsystem.payload.request.SignupRequest;
import com.it120p.librarymanagementsystem.payload.response.JwtResponse;
import com.it120p.librarymanagementsystem.payload.response.MessageResponse;
import com.it120p.librarymanagementsystem.repository.RoleRepository;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import com.it120p.librarymanagementsystem.security.jwt.JwtUtils;
import com.it120p.librarymanagementsystem.security.services.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.it120p.librarymanagementsystem.security.services.impl.EmailServiceImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AuthController class manages the authentication operations for User entities.
 * It uses the UserRepository and RoleRepository to interact with the database.
 * It also uses the AuthenticationManager, PasswordEncoder, and JwtUtils for authentication and authorization.
 */

/**
 * @CrossOrigin annotation is used to handle the response headers to allow the
 * client-side to access the response from a different origin.
 * It is used to handle the communication between different servers.
 *
 * @RestController annotation is used to create RESTful web services using
 * Spring MVC.
 *
 * @RequestMapping annotation is used to map web requests to Spring Controller
 * methods. It is used at the class level to indicate the base URL for all
 * the web requests handled by the controller.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * @Autowired annotation is used for automatic dependency injection.
     * We use it to avoid creating getters and setters.
     * AuthenticationManager is an interface which provides the authenticate()
     * method. It is used to authenticate the user.
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * UserRepository is an interface which extends JpaRepository. It will
     * perform all the CRUD operations on User entity.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * RoleRepository is an interface which extends JpaRepository. It will
     * perform all the CRUD operations on Role entity.
     */
    @Autowired
    RoleRepository roleRepository;

    /**
     * PasswordEncoder is an interface which provides the encode() method.
     * It is used to perform one-way encryption on passwords.
     */
    @Autowired
    PasswordEncoder encoder;

    /**
     * EmailServiceImpl is a class which provides the sendSimpleMailMessage() method.
     */
    @Autowired
    EmailServiceImpl emailServiceImpl;

    /**
     * JwtUtils is a class which provides the generateJwtToken() method.
     * It is used to generate a JWT token for the user.
     */
    @Autowired
    JwtUtils jwtUtils;

    /**
     * Authenticates a User entity and generates a JWT token for the authenticated user.
     *
     * @param loginRequest the login request containing the username and password.
     * @return a ResponseEntity containing the JWT response with the token and user details.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Registers a new User entity and saves it to the database.
     *
     * @param signUpRequest the signup request containing the user details.
     * @return a ResponseEntity containing the message response.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if the username is already taken
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            // Return a bad request response with the message
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if the email is already in use
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            // Return a bad request response with the message
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create a new User entity with the details from the signup request
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                // Encode the password using the PasswordEncoder
                encoder.encode(signUpRequest.getPassword()));

        // Set the roles for the user
        Set<String> strRoles = signUpRequest.getRole();
        // Create a new HashSet to store the roles
        Set<Role> roles = new HashSet<>();

        // Check if the roles are null, if they are, then add the ROLE_USER role
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            // If the roles are not null, then add the roles to the user
            strRoles.forEach(role -> {
                switch (role) {
                    // If the role is an admin role, then add the ROLE_ADMIN role
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        // Set the roles for the user
        user.setRoles(roles);
        // Save the user to the database
        userRepository.save(user);
        emailServiceImpl.sendSimpleMailMessage(user.getEmail(), "New User Account Created", "Your account has been created successfully!", user.getName());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}