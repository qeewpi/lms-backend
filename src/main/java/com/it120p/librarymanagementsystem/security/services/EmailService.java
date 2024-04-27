package com.it120p.librarymanagementsystem.security.services;

import com.it120p.librarymanagementsystem.model.Order;

public interface EmailService {
    void sendSimpleMailMessage(String to, String subject, String text, String name);
    void sendMimeMessageWithAttachment(String to, String subject, String text, String pathToAttachment, String name);
    void sendMimeMessageWithEmbeddedImage(String to, String subject, String text, String pathToAttachment, String name);
    void sendMimeMessageWithEmbeddedFile(String to, String subject, String text, String pathToAttachment, String name);
    void sendHtmlEmail(String to, String subject, String text, String name, Order order);
    void sendHtmlEmailWithAttachment(String to, String subject, String text, String pathToAttachment, String name);
    void sendHtmlEmailWithEmbeddedImage(String to, String subject, String text, String pathToAttachment, String name);
    void sendHtmlEmailWithEmbeddedFile(String to, String subject, String text, String pathToAttachment, String name);
}
