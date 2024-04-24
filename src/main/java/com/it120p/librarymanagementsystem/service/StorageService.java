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

@Service
public class StorageService {

    @Autowired
    private BookRepository bookRepository;

    // Change the path to your desired directory
    private final String IMAGE_PATH = "C:/Mapua/3Q2324/IT120P/lms-backend/uploads";

    public String uploadImageToFileSystem(MultipartFile file, String bookTitle) throws IOException {
        // Replace spaces with underscores and remove special characters
        String sanitizedTitle = bookTitle.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", "");
        String imagePath = IMAGE_PATH + "/" + sanitizedTitle + ".png";

        file.transferTo(new File(imagePath));
        return imagePath;
    }

    public byte[] downloadImageFromFileSystem (String imageName) throws IOException {
        Optional<Book> book = bookRepository.findByImagePathContaining(imageName);
        if (!book.isPresent()) {
            throw new IOException("No book found with image name: " + imageName);
        }
        String imagePath = book.get().getImagePath();

        byte[] images = Files.readAllBytes(new File(imagePath).toPath());
        return images;
    }
}
