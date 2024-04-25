package com.it120p.librarymanagementsystem.service;

import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * The StorageService class is a service class that handles the storage of book images in the file system.
 *
 * This class is marked with the @Service annotation, meaning that it is a candidate for Spring's component scanning to detect and add to the application context.
 *
 * The BookRepository is autowired and used to fetch book data from the database.
 *
 * The uploadImageToFileSystem method is used to upload a book image to the file system. It takes a MultipartFile and a book title as parameters, and returns the path of the uploaded image.
 *
 * The downloadImageFromFileSystem method is used to download a book image from the file system. It takes an image name as a parameter, and returns the image as a byte array.
 */
@Service
public class StorageService {

    @Autowired
    private BookRepository bookRepository;

    // Change the path to your desired directory
    private final String IMAGE_PATH = "C:/Mapua/3Q2324/IT120P/lms-backend/uploads";

    /**
     * Uploads a book image to the file system.
     *
     * Multipart file is used to handle file uploads in Spring.
     *
     * @param file the MultipartFile to be uploaded.
     * @param bookTitle the title of the book.
     * @return the path of the uploaded image.
     * @throws IOException if an error occurs during file transfer.
     */
    public String uploadImageToFileSystem(MultipartFile file, String bookTitle) throws IOException {
        // Replace spaces with underscores and remove special characters
        // to create a sanitized file name
        String sanitizedTitle = bookTitle.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", "");
        String imagePath = IMAGE_PATH + "/" + sanitizedTitle + ".png";

        // Transfer the file to the specified path
        file.transferTo(new File(imagePath));
        return imagePath;
    }

    /**
     * Downloads a book image from the file system using the image name.
     *
     * A byte array is used to store the image data.
     *
     * @param imageName the name of the image to be downloaded.
     * @return the image as a byte array.
     * @throws IOException if an error occurs during file reading.
     */
    public byte[] downloadImageFromFileSystem (String imageName) throws IOException {
        // Find the book with the specified image name
        // bookRepository is used to fetch book data from the database
        Optional<Book> book = bookRepository.findByImagePathContaining(imageName);
        if (!book.isPresent()) {
            throw new IOException("No book found with image name: " + imageName);
        }
        String imagePath = book.get().getImagePath();

        // Read the image as a byte array to be returned and displayed
        byte[] images = Files.readAllBytes(new File(imagePath).toPath());
        return images;
    }
}
