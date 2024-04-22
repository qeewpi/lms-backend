package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.OrderNotFoundException;
import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/order")
    Order newOrder(@RequestBody Order newOrder) {return orderRepository.save(newOrder);
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
                    order.setOrder_date(newOrder.getOrder_date());
                    order.setReturn_date(newOrder.getReturn_date());
                    order.setDue_date(newOrder.getDue_date());
                    order.setUser(newOrder.getUser());
                    order.setBook(newOrder.getBook());
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
