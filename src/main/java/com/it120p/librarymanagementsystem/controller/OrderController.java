package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.BookNotFoundException;
import com.it120p.librarymanagementsystem.exception.OrderNotFoundException;
import com.it120p.librarymanagementsystem.exception.UserNotFoundException;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.model.OrderStatus;
import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.BookRepository;
import com.it120p.librarymanagementsystem.repository.OrderRepository;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The OrderController class manages the CRUD operations for Order entities.
 * It uses the OrderRepository, UserRepository, and BookRepository to interact with the database.
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Creates a new Order entity and saves it to the database.
     *
     * @param newOrder the Order entity to be created.
     * @return the created Order entity.
     * @throws UserNotFoundException if the User entity associated with the order is not found.
     * @throws BookNotFoundException if any of the Book entities associated with the order are not found.
     */
    @PostMapping("/order")
    Order newOrder(@RequestBody Order newOrder) {
        // Check if the user exists using the user ID
        User user = userRepository.findById(newOrder.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(newOrder.getUser().getId()));

        /* Check if the books exist using the book IDs
         * stream() is used to convert the list of books to a stream
         * The books have to be a stream in order to use the map() function
         * map() is used to convert each book ID to a Book entity
         */
        List<Book> books = newOrder.getBooks().stream()
                .map(book -> bookRepository.findById(book.getId())
                        .orElseThrow(() -> new BookNotFoundException(book.getId())))
                // Collect the stream of Book entities back to a list using
                // Collectors.toList()
                .collect(Collectors.toList());

        // Set the books and user for the new order
        // Save the new order to the database
        newOrder.setBooks(books);
        newOrder.setUser(user);
        return orderRepository.save(newOrder);
    }

    /**
     * Creates new Order entities and saves them to the database.
     *
     * @param newOrders the list of Order entities to be created.
     * @return the list of created Order entities.
     */
    @PostMapping("/orders")
    Iterable<Order> newOrders(@RequestBody Iterable<Order> newOrders) {
        return orderRepository.saveAll(newOrders);
    }

    /**
     * Retrieves all Order entities from the database.
     *
     * @return the list of all Order entities.
     */
    @GetMapping("/orders")
    Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves an Order entity by its ID.
     *
     * @param id the ID of the Order entity to be retrieved.
     * @return the Order entity with the given ID.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Retrieves the User entity associated with an Order entity by the order's ID.
     *
     * @param orderId the ID of the Order entity.
     * @return the User entity associated with the Order entity.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @GetMapping("/order/{orderId}/user")
    public User getUserByOrderId(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return order.getUser();
    }

    /**
     * Updates an Order entity with the given ID.
     *
     * @param newOrder the new data for the Order entity.
     * @param id the ID of the Order entity to be updated.
     * @return the updated Order entity.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @PutMapping("/order/{id}")
    Order updateOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setBooks(newOrder.getBooks());
                    order.setUser(newOrder.getUser());
                    order.setBorrowed_at(newOrder.getBorrowed_at());
                    order.setDue_date(newOrder.getDue_date());
                    order.setReturned_at(newOrder.getReturned_at());

                    // Check if the order is overdue
                    if (order.isOverdue()) {
                        order.setStatus(OrderStatus.OVERDUE);
                    }

                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Marks an Order entity as returned and updates the return date to the current date.
     *
     * @param orderId the ID of the Order entity to be returned.
     * @return the updated Order entity.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @PutMapping("/order/return/{orderId}")
    public Order returnOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                // Order is mapped to a new Order entity
                // with the returned_at date set to the current date
                // to keep track of when the order was returned
                .map(order -> {
                    order.setReturned_at(new Date());
                    order.setStatus(OrderStatus.RETURNED);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    /**
     * Deletes an Order entity with the given ID.
     *
     * @param id the ID of the Order entity to be deleted.
     * @return a message indicating the Order entity has been deleted.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
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
