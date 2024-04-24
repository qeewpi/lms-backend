package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.BookNotFoundException;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import com.it120p.librarymanagementsystem.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private StorageService service;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/book")
    public ResponseEntity<?> newBook(@RequestParam("image") MultipartFile file, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre, @RequestParam("description") String description) throws IOException {
        String imagePath = service.uploadImageToFileSystem(file, title);
        Book newBook = Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .description(description)
                .imagePath(imagePath)
                .build();
        Book savedBook = bookRepository.save(newBook);
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedBook);
    }

    @PostMapping("/books")
    List<Book> newBooks(@RequestBody List<Book> newBooks) {
        return bookRepository.saveAll(newBooks);
    }

    @GetMapping("/books")
    List<Book> listAllBooks() {
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
                    book.setGenre(newBook.getGenre());
                    book.setDescription(newBook.getDescription());
                    book.setImagePath(newBook.getImagePath());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @DeleteMapping("/book/{id}")
    ResponseEntity<String> deleteBook(@PathVariable Long id) {
    if(bookRepository.existsById(id)){
        try {
            bookRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Book with ID: " + id + " has been deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while trying to delete book with ID: " + id + ". Error message: " + e.getMessage());
        }
    } else {
        throw new BookNotFoundException(id);
    }
}

    @GetMapping("/book/download/{imageName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String imageName) throws IOException {
        byte[] images = service.downloadImageFromFileSystem(imageName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(images);
    }
}