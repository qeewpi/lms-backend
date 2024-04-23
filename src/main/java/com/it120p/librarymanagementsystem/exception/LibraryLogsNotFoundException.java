package com.it120p.librarymanagementsystem.exception;

public class LibraryLogsNotFoundException extends RuntimeException {
    public LibraryLogsNotFoundException(Long id) {
        super("Could not find admin with the ID: " + id);
    }
}
