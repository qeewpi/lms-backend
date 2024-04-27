package com.it120p.librarymanagementsystem.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.model.OrderStatus;
import com.it120p.librarymanagementsystem.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        System.out.println("The time is now " + dateFormat.format(new Date()));
//    }

    // every one sec for testing
    //    @Scheduled(fixedRate = 10000)
    // This will run the task every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateOverdueOrders() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if (order.isOverdue() && order.getStatus() != OrderStatus.OVERDUE) {
                order.setStatus(OrderStatus.OVERDUE);
                orderRepository.save(order);
                System.out.println("Order " + order.getId() + " is now overdue.");
            }
        }
    }

    public void createOverdueOrderForTesting() {
        Order order = new Order();
        order.setBorrowed_at(new Date(System.currentTimeMillis() - 8 * 24 * 60 * 60 * 1000)); // 8 days ago
        order.setDue_date(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)); // 7 days ago
        order.setStatus(OrderStatus.BORROWED);
        orderRepository.save(order);
    }
}
