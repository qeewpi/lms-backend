package com.it120p.librarymanagementsystem.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not find order with the ID: " + id);
    }
}
