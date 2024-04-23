package com.it120p.librarymanagementsystem.model;
import jakarta.persistence.*;

@Entity
public class LibraryLogs {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private User user;
    private Book book;
    private String timestamps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }
}
