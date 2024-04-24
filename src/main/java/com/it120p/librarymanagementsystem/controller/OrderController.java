package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.BookNotFoundException;
import com.it120p.librarymanagementsystem.exception.OrderNotFoundException;
import com.it120p.librarymanagementsystem.exception.UserNotFoundException;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import com.it120p.librarymanagementsystem.repository.OrderRepository;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/order")
    Order newOrder(@RequestBody Order newOrder) {
        User user = userRepository.findById(newOrder.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(newOrder.getUser().getId()));

        List<Book> books = newOrder.getBooks().stream()
                .map(book -> bookRepository.findById(book.getId())
                        .orElseThrow(() -> new BookNotFoundException(book.getId())))
                .collect(Collectors.toList());

        newOrder.setBooks(books);
        newOrder.setUser(user);
        return orderRepository.save(newOrder);
    }

    @PostMapping("/orders")
    Iterable<Order> newOrders(@RequestBody Iterable<Order> newOrders) {
        return orderRepository.saveAll(newOrders);
    }

    @GetMapping("/orders")
    Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PutMapping("/order/{id}")
    Order updateOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setBooks(newOrder.getBooks());
                    order.setUser(newOrder.getUser());
                    order.setCreated_at(newOrder.getCreated_at());
                    order.setBorrowed_at(newOrder.getBorrowed_at());

                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @DeleteMapping("/order/{id}")
    String deleteOrder(@PathVariable Long id) {
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            return "Order with ID: " + id + " has been deleted.";
        } else {
            throw new OrderNotFoundException(id);
        }
    }
}
