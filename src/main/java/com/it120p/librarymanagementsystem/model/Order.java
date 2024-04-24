package com.it120p.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Entity
@Table (name = "orders")
public class Order {
    @Setter
    @Getter
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "order_books",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Setter
    @Getter
    @Column(name = "borrowed_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowed_at;

    @PrePersist
    protected void onCreate() {
        borrowed_at = new Date();
        status = OrderStatus.BORROWED;

        Calendar cal = Calendar.getInstance();
        cal.setTime(borrowed_at);
        cal.add(Calendar.DAY_OF_MONTH, 7); // 7 days borrowing period
        due_date = cal.getTime();
    }

    @Setter
    @Getter
    private Date due_date;

    @Getter
    private Date returned_at;

    public void setBorrowed_at(Date borrowed_at) {
        this.borrowed_at = borrowed_at;

        if (borrowed_at != null) {
            this.status = OrderStatus.BORROWED;
        }
    }

    public void setReturned_at(Date returned_at) {
        this.returned_at = returned_at;

        if (returned_at != null) {
            this.status = OrderStatus.RETURNED;
        }
    }

    public boolean isOverdue() {
        if (status != OrderStatus.BORROWED || borrowed_at == null) {
            return false;
        }

        return new Date().after(due_date);
    }
}
