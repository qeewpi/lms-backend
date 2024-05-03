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
import com.it120p.librarymanagementsystem.security.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private EmailService emailService;

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

        Order savedOrder = orderRepository.save(newOrder);

        // Send an email to the user to confirm the order
        emailService.sendHtmlEmail(
                newOrder.getUser().getEmail(),
                "Order Confirmation",
                "Your order has been confirmed.",
                newOrder.getUser().getName(),
                newOrder
        );
        return savedOrder;

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/orders")
    Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves all Order entities associated with a User entity by the user's ID.
     * If the authenticated user is an admin, they can retrieve orders for any user.
     * If the authenticated user is a regular user, they can only retrieve their own orders.
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/user/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException(currentPrincipalName));

        // If the user is not the owner of the orders and does not have the 'ROLE_ADMIN' role, throw an AccessDeniedException
        if (!user.getId().equals(userId) && !user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You do not have permission to access these orders.");
        }

        // Retrieve the orders for the user
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders;
    }

    /**
     * Retrieves an Order entity by its ID.
     *
     * @param id the ID of the Order entity to be retrieved.
     * @return the Order entity with the given ID.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException(currentPrincipalName));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!order.getUser().getId().equals(user.getId()) && !user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        return order;
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
                    order.setPickedUp(newOrder.isPickedUp());

                    // Check if the new status is OVERDUE
                    if (newOrder.getStatus() == OrderStatus.OVERDUE) {
                        order.setStatus(OrderStatus.OVERDUE);
                    } else if (order.isOverdue()) {
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

    /** Marks an Order entity as picked up.
     *
     * @param orderId the ID of the Order entity to be marked as picked up.
     * @return the updated Order entity.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @PutMapping("/order/pickup/{orderId}")
    public Order pickupOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                // Order is mapped to a new Order entity
                // with the returned_at date set to the current date
                // to keep track of when the order was returned
                .map(order -> {
                    order.setPickedUp(true);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    /** Marks an Order entity as overdue.
     *
     * @param orderId the ID of the Order entity to be marked as overdue.
     * @return the updated Order entity.
     * @throws OrderNotFoundException if no Order entity with the given ID is found.
     */
    @PutMapping("/order/overdue/{orderId}")
    public Order overdueOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                // Order is mapped to a new Order entity
                // with the returned_at date set to the current date
                // to keep track of when the order was returned
                .map(order -> {
                    order.setStatus(OrderStatus.OVERDUE);
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

    /**
     * Renews an Order entity with the given ID, retaining the same order details but updating the due date.
     *
     * @param id the ID of the Order entity to be renewed.
     * @return the renewed Order entity.
     */
    @PutMapping("/order/renew/{id}")
    Order renewOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException(currentPrincipalName));

        Order orderInfo = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!orderInfo.getUser().getId().equals(user.getId()) && !user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        return orderRepository.findById(id)
                .map(order -> {
                    LocalDateTime existingDueDate = LocalDateTime.ofInstant(order.getDue_date().toInstant(), ZoneId.systemDefault());

                    // Calculate the remaining days to the due date
                    long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), existingDueDate);

                    // Can comment this out for testing
                    // If the remaining days are greater than 2, throw an exception
//                    if (remainingDays > 2) {
//                        throw new IllegalStateException("Cannot renew order. The remaining days must be less than or equal to 2.");
//                    }

                    LocalDateTime dueDate = existingDueDate.plusDays(5);
                    Date dueDateAsDate = Date.from(dueDate.atZone(ZoneId.systemDefault()).toInstant());
                    order.setDue_date(dueDateAsDate);

                    emailService.sendSimpleMailMessage(
                            order.getUser().getEmail(),
                            "Order Renewal",
                            "Your order with ID: " + order.getId() + " has been renewed.",
                            order.getUser().getName()
                    );

                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Renews an Order entity with specific Order ID and Book ID(s) by creating a new order with the same user and specified
     * book Id(s)
     * The order is renewed by setting the due date to 5 days from the current date
     *
     * @param orderId the ID of the Order entity to be renewed
     * @param bookId the ID of the Book entity to be renewed
     */
    @PostMapping("/order/renew-books/{orderId}")
    public Order renewOrder(@PathVariable Long orderId, @RequestBody List<Long> bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException(currentPrincipalName));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUser().getId().equals(user.getId()) && !user.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        List<Book> books = bookId.stream()
                .map(id -> bookRepository.findById(id)
                        .orElseThrow(() -> new BookNotFoundException(id)))
                .collect(Collectors.toList());

        // Convert existing due date from Date to LocalDateTime
        LocalDateTime existingDueDate = LocalDateTime.ofInstant(order.getDue_date().toInstant(), ZoneId.systemDefault());

        // Calculate the remaining days to the due date
        long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), existingDueDate);

        // Can comment this out for testing
        // If the remaining days are less than 2, throw an exception
//        if (remainingDays >= 2) {
//            throw new IllegalStateException("Cannot renew order. The remaining days must be less than or equal to 2.");
//        }

        // Add 5 days to the existing due date
        LocalDateTime newDueDate = existingDueDate.plusDays(5);

        // Convert the new due date from LocalDateTime back to Date
        Date newDueDateAsDate = Date.from(newDueDate.atZone(ZoneId.systemDefault()).toInstant());

        Order newOrder = new Order();
        newOrder.setUser(order.getUser());
        newOrder.setBooks(books);
        newOrder.setBorrowed_at(new Date());
        newOrder.setDue_date(newDueDateAsDate);
        newOrder.setPickedUp(false);
        newOrder.setStatus(OrderStatus.BORROWED);

        Order savedOrder = orderRepository.save(newOrder);

        List<String> bookTitles = savedOrder.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        String bookTitlesString = String.join(", ", bookTitles);

        emailService.sendSimpleMailMessage(
                newOrder.getUser().getEmail(),
                "Order Renewal",
                "Your order has been renewed. Your new order ID is: " + savedOrder.getId() + ", with the book(s): " + bookTitlesString + ".",
                newOrder.getUser().getName()
        );

        return savedOrder;
    }
}
