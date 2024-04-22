package com.it120p.librarymanagementsystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not find user with the ID: " + id);
    }
}
