package com.it120p.librarymanagementsystem.security.services.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.it120p.librarymanagementsystem.model.Book;
import com.it120p.librarymanagementsystem.model.Order;
import com.it120p.librarymanagementsystem.security.services.EmailService;
import com.it120p.librarymanagementsystem.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String NEW_USER_ACCOUNT_CREATED = "New User Account Created";

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;

    private final StorageService storageService;

    @Override
    public void sendSimpleMailMessage(String to, String subject, String text, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_CREATED);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText("Hello " + name + ",\n\n" + text + "\n\n" + "Thank you for using our service.\n\n" + "Best Regards,\n" + "Library Management System");

            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }

    @Override
    public void sendMimeMessageWithAttachment(String to, String subject, String text, String pathToAttachment, String name) {

    }

    @Override
    public void sendMimeMessageWithEmbeddedImage(String to, String subject, String text, String pathToAttachment, String name) {

    }

    @Override
    public void sendMimeMessageWithEmbeddedFile(String to, String subject, String text, String pathToAttachment, String name) {

    }

    @Override
    public void sendHtmlEmail(String to, String subject, String text, String name, Order order) {
        try {
            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("orderId", order.getId());
            context.setVariable("name", order.getUser().getName());
            context.setVariable("orderStatus", order.getStatus());
            context.setVariable("orderDate", order.getBorrowed_at());
            context.setVariable("dueDate", order.getDue_date());
            context.setVariable("books", order.getBooks());

            // Add the book images
            for (Book book : order.getBooks()) {
                byte[] imageBytes = storageService.downloadImageFromFileSystem(book.getImagePath());
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                context.setVariable("image_" + book.getId(), imageBase64);
            }

            String process = templateEngine.process("emailTemplate", context);

            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(fromEmail);
                messageHelper.setTo(to);
                messageHelper.setSubject("Order Summary - Library Management System");
                messageHelper.setText(process, true);
            };

            emailSender.send(messagePreparator);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }

    @Override
    public void sendHtmlEmailWithAttachment(String to, String subject, String text, String pathToAttachment, String name) {

    }

    @Override
    public void sendHtmlEmailWithEmbeddedImage(String to, String subject, String text, String pathToAttachment, String name) {

    }

    @Override
    public void sendHtmlEmailWithEmbeddedFile(String to, String subject, String text, String pathToAttachment, String name) {

    }
}
