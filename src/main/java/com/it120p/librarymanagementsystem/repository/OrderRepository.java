package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The OrderRepository interface is a Spring Data JPA repository for Order entities.
 *
 * In this case, the repository is for Order entities, and the ID of the Order entity is of type Long.
 */
public interface OrderRepository extends JpaRepository<Order, Long>{
}
