package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @PostMapping("/users")
    List<User> newUsers(@RequestBody List<User> newUsers) {
        return userRepository.saveAll(newUsers);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
