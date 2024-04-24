package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>{
    Optional<Book> findByImagePathContaining(String imageName);}
