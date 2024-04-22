package com.it120p.librarymanagementsystem.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Could not find admin with the ID: " + id);
    }
}
