package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final OrderService orderService;

    public TestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/testOverdueOrder/{userId}")
    public void testOverdueOrder(@PathVariable Long userId) {
        orderService.createOverdueOrderForTesting(userId);
    }

    @GetMapping("/testOneDayBeforeDueDate/{userId}")
    public void testOneDayBeforeDueDate(@PathVariable Long userId) {
        orderService.createOrderDueInOneDayForTesting(userId);
    }
}
