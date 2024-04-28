package com.it120p.librarymanagementsystem.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.model.OrderStatus;
import com.it120p.librarymanagementsystem.model.User;
import com.it120p.librarymanagementsystem.repository.OrderRepository;
import com.it120p.librarymanagementsystem.repository.UserRepository;
import com.it120p.librarymanagementsystem.security.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //    @Scheduled(fixedRate = 5000)
    //    public void reportCurrentTime() {
    //        System.out.println("The time is now " + dateFormat.format(new Date()));
    //    }

    // every one sec for testing
    // This will run the task every day at midnight
    /**
     * This method will update the status of orders that are overdue.
     * It will run every day at midnight.
     * It will check all orders in the database and update the status of orders that are overdue.
     */
    // @Scheduled(fixedRate = 10000)
    // for testing purposes
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverdueOrders() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if (order.isOverdue() && order.getStatus() != OrderStatus.OVERDUE) {
                order.setStatus(OrderStatus.OVERDUE);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " is now overdue.");
                emailService.sendSimpleMailMessage(order.getUser().getEmail(), "Order Overdue",
                        "Your order with ID " + order.getId() + " is now overdue. Please return the book(s) as soon as possible.",
                        order.getUser().getName());
            }
        }
    }

    /**
     * This method is a scheduled service that sends an email to users that have
     * orders with 1 day left before the due date.
     * It will run every day at midnight.
     */
    // @Scheduled(fixedRate = 10000)
    // for testing purposes
    @Scheduled (cron = "0 0 0 * * ?")
    public void notifyOrdersDueInOneDay() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if (order.getDaysRemaining() == 1) {
                User user = order.getUser();
                emailService.sendSimpleMailMessage(user.getEmail(), "Order Due Tomorrow",
                        "Your order with ID " + order.getId() + " is due tomorrow. Please return the book(s) on time.",
                        user.getName());
            }
        }
    }

    /**
     * To force create orders, you need to comment out the
     * onCreate() method in the Order class.
     */

    /**
     * This method will create an overdue order for testing purposes.
     * It will create an order that is overdue by 1 day.
     */
    public void createOverdueOrderForTesting(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setBorrowed_at(new Date(System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000)); // 8 days ago
        order.setDue_date(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)); // 7 days ago
        order.setStatus(OrderStatus.BORROWED);
        orderRepository.save(order);
    }

    /**
     * This method will create an order with 1 day left remaining till
     * due date for testing purposes.
     */
    public void createOrderDueInOneDayForTesting(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setBorrowed_at(new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000)); // 6 days ago
        order.setDue_date(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000)); // 1 day from now
        order.setStatus(OrderStatus.BORROWED);
        orderRepository.save(order);
    }
}
