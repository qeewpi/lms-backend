package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
