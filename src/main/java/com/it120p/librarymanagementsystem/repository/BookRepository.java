package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The BookRepository interface is a Spring Data JPA repository for Book entities.
 *
 * Spring Data JPA repositories are interfaces with methods supporting creating, reading, updating, and deleting records against a back end data store.
 * Some common types of functionality provided by Spring Data JPA repositories include standard CRUD operations, pagination and sorting, and declarative query methods.
 *
 * This repository uses the JpaRepository which provides JPA related methods such as save(), findOne(), findAll(), count(), delete() etc.
 * You can also define other query methods as needed.
 *
 * In this case, the repository is for Book entities, and the ID of the Book entity is of type Long.
 *
 * The findByImagePathContaining method is a query method that finds a book by its image path containing a specific string.
 */
public interface BookRepository extends JpaRepository<Book, Long>{
    /**
     * Finds a book by its image path containing a specific string.
     *
     * Optional is used because the result may be null.
     *
     * @param imageName the specific string to be searched in the image path.
     * @return an Optional of Book that contains the book if found, or an empty Optional if not.
     */
    Optional<Book> findByImagePathContaining(String imageName);}
