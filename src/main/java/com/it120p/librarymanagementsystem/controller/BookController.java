package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.BookNotFoundException;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.model.EGenre;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import com.it120p.librarymanagementsystem.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

/**
 * The BookController class manages the CRUD operations for Book entities.
 * It uses the BookRepository to interact with the database.
 * It also uses the StorageService for handling book images.
 */
@RestController
/** @CrossOrigin is used to handle the request from a different origin.
 * The value "http://localhost:3000" is the URL of the React Frontend
 */
@CrossOrigin("http://localhost:3000")
public class BookController {
    @Autowired
    private StorageService service;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Creates a new Book entity and saves it to the database.
     * The book's image is stored using the StorageService.
     *
     * @param file the image of the book.
     * @param title the title of the book.
     * @param author the author of the book.
     * @param genre the genre of the book.
     * @param description the description of the book.
     * @return the created Book entity.
     * @throws IOException if an error occurs while storing the image.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<?> newBook(@RequestParam("image") MultipartFile file, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre, @RequestParam("description") String description) throws IOException {
        // Save the image to the file system
        String imagePath = service.uploadImageToFileSystem(file, title);
        // Convert the String genre to an EGenre enum
        EGenre eGenre = EGenre.valueOf(genre.toUpperCase());
        // Create the new Book entity
        Book newBook = Book.builder()
                .title(title)
                .author(author)
                .genre(eGenre)
                .description(description)
                .imagePath(imagePath)
                .build();
        Book savedBook = bookRepository.save(newBook);
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedBook);
    }

    /**
     * Creates new Book entities and saves them to the database.
     *
     * @param newBooks the list of Book entities to be created.
     * @return the list of created Book entities.
     */
    @PostMapping("/books")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Book> newBooks(@RequestBody List<Book> newBooks) {
        return bookRepository.saveAll(newBooks);
    }

    /**
     * Retrieves all Book entities from the database.
     *
     * @return the list of all Book entities.
     */
    @GetMapping("/books")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a Book entity by its ID from the database.
     *
     * @param id the ID of the Book entity.
     * @return the Book entity with the specified ID.
     * @throws BookNotFoundException if the Book entity is not found.
     */
    @GetMapping("/book/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Updates a Book entity by its ID in the database.
     *
     * @param id the ID of the Book entity.
     * @return the updated Book entity.
     * @throws BookNotFoundException if the Book entity is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/book/{id}")
    public ResponseEntity<?> editBook(@PathVariable Long id, @RequestParam(value = "image", required = false) Optional<MultipartFile> file, @RequestParam("title") String title, @RequestParam("author") String author, @RequestParam("genre") String genre, @RequestParam("description") String description) throws IOException {
        // Find the book by its ID
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        String imagePath = bookToUpdate.getImagePath(); // Use the existing image path by default

        // Check if a new image has been provided
        if (file.isPresent()) {
            // Save the new image to the file system
            imagePath = service.uploadImageToFileSystem(file.get(), title);
        }

        // Convert the String genre to an EGenre enum
        EGenre eGenre = EGenre.valueOf(genre.toUpperCase());

        // Update the book details
        bookToUpdate.setTitle(title);
        bookToUpdate.setAuthor(author);
        bookToUpdate.setGenre(eGenre);
        bookToUpdate.setDescription(description);
        bookToUpdate.setImagePath(imagePath);

        // Save the updated book to the database
        Book updatedBook = bookRepository.save(bookToUpdate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedBook);
    }

    /**
     * Deletes a Book entity by its ID from the database.
     *
     * @param id the ID of the Book entity.
     * @return a ResponseEntity with a message indicating the result of the operation.
     * @throws BookNotFoundException if the Book entity is not found.
     */
    @DeleteMapping("/book/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    /**
     * Downloads the image of a Book entity from the file system.
     *
     * @param imageName the name of the image file.
     * @return the image file.
     * @throws IOException if an error occurs while downloading the image.
     */
    @GetMapping("/book/download/{imageName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String imageName) throws IOException {
        // Download the image from the file system using the StorageService
        // byte[] is used to store the image data
        byte[] images = service.downloadImageFromFileSystem(imageName);
        // Return the image as a ResponseEntity
        return ResponseEntity.status(HttpStatus.OK)
                // Set the content type of the response to image/png
                .contentType(MediaType.valueOf("image/png"))
                // Set the content length of the response to the length of the image data
                .body(images);
    }
}