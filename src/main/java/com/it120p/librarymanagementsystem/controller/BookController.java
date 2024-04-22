package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.BookNotFoundException;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/book")
    Book newBook(@RequestBody Book newBook) {
        return bookRepository.save(newBook);
    }

    @PostMapping("/books")
    Iterable<Book> newBooks(@RequestBody Iterable<Book> newBooks) {
        return bookRepository.saveAll(newBooks);
    }

    @GetMapping("/books")
    Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/book/{id}")
    Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PutMapping("/book/{id}")
    Book updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    book.setDescription(newBook.getDescription());
                    book.setImage_url(newBook.getImage_url());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @DeleteMapping("/book/{id}")
    String deleteBook(@PathVariable Long id) {
        if(bookRepository.existsById(id)){
            bookRepository.deleteById(id);
            return "Book with ID: " + id + " has been deleted.";
        } else {
            throw new BookNotFoundException(id);
        }
    }
}
