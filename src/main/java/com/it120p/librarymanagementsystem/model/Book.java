package com.it120p.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Book class is an entity model object for a Book.
 * It contains fields for the book's ID, title, author, genre, description, and image path.
 * The ID is generated automatically when a Book entity is created.
 * The description field can hold up to 5000 characters.
 */
@Entity
// Used Lombok @Data to automatically generate getters and setters for all fields
@Data
// Used Lombok @AllArgsConstructor to generate a constructor with all arguments
@AllArgsConstructor
// Used Lombok @NoArgsConstructor to generate a default constructor
@NoArgsConstructor
// Used Lombok @Builder to automatically generate a builder pattern for the entity
// Builder pattern is used in this case to create a Book object with the specified fields
@Builder
public class Book {
    /** The ID of the book */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    /** The title of the book */
    private String title;

    /** The author of the book */
    private String author;

    /** The genre of the book */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EGenre genre;

    /** The description of the book
     * The description can hold up to 5000 characters
     */
    @Column(length = 5000)
    private String description;

    /** The image path of the book */
    private String imagePath;
}
