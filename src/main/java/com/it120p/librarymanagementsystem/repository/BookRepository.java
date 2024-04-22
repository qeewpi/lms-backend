package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>{
}
