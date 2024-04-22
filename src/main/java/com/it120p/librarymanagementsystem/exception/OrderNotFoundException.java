package com.it120p.librarymanagementsystem.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not find admin with the ID: " + id);
    }
}
