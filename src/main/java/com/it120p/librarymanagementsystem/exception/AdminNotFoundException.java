package com.it120p.librarymanagementsystem.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long id) {
        super("Could not find admin with the ID: " + id);
    }
}
