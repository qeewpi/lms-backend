package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
