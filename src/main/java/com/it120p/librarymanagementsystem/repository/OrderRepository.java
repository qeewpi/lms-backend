package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>{
}
