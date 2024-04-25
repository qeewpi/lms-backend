package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The AdminRepository interface is a Spring Data JPA repository for Admin entities.
 *
 * Spring Data JPA repositories are interfaces with methods supporting creating, reading, updating, and deleting records against a back end data store.
 * Some common types of functionality provided by Spring Data JPA repositories include standard CRUD operations, pagination and sorting, and declarative query methods.
 *
 * This repository uses the JpaRepository which provides JPA related methods such as save(), findOne(), findAll(), count(), delete() etc.
 * You can also define other query methods as needed.
 *
 * In this case, the repository is for Admin entities, and the ID of the Admin entity is of type Long.
 */
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
