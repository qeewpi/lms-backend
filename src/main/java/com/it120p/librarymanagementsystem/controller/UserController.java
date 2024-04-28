package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.UserNotFoundException;
import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import com.it120p.librarymanagementsystem.security.services.impl.EmailServiceImpl;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The UserController class manages the CRUD operations for User entities.
 * It uses the UserRepository to interact with the database.
 * All methods in this class require the user to have the 'ADMIN' role.
 */
@RestController
@CrossOrigin("http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * PasswordEncoder is an interface which provides the encode() method.
     * It is used to perform one-way encryption on passwords.
     */
    @Autowired
    PasswordEncoder encoder;

    /**
     * Creates a new User entity and saves it to the database.
     *
     * @param newUser the User entity to be created.
     * @return the created User entity.
     */
    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    /**
     * Creates new User entities and saves them to the database.
     *
     * @param newUsers the list of User entities to be created.
     * @return the list of created User entities.
     */
    @PostMapping("/users")
    List<User> newUsers(@RequestBody List<User> newUsers) {
        return userRepository.saveAll(newUsers);
    }

    /**
     * Retrieves all User entities from the database.
     *
     * @return the list of all User entities.
     */
    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a User entity by its ID.
     *
     * @param id the ID of the User entity to be retrieved.
     * @return the User entity with the given ID.
     * @throws UserNotFoundException if no User entity with the given ID is found.
     */
    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Updates a User entity with the given ID.
     *
     * @param newUser the new data for the User entity.
     * @param id the ID of the User entity to be updated.
     * @return the updated User entity.
     * @throws UserNotFoundException if no User entity with the given ID is found.
     */
    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(encoder.encode(newUser.getPassword()));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Deletes a User entity with the given ID.
     *
     * @param id the ID of the User entity to be deleted.
     * @return a message indicating the User entity has been deleted.
     * @throws UserNotFoundException if no User entity with the given ID is found.
     */
    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return "User with ID: " + id + " has been deleted.";
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
