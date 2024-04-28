package com.it120p.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Order class is an entity model object for an Order.
 * It contains fields for the order's ID, user, books, status, borrowed date, due date, and returned date.
 * The ID is generated automatically when an Order entity is created.
 * The borrowed date is set to the current date when an Order entity is created.
 * The status is set to BORROWED when an Order entity is created.
 * The due date is set to 7 days after the borrowed date when an Order entity is created.
 */
@Entity
@Table (name = "orders")
public class Order {
    @Setter
    @Getter
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    /* The relationship of Order to User is Many-to-One.
        * An Order can have only one User.
        * A User can have multiple Orders.
        * The user_id column is a foreign key that references the id column in the users table.
        * The @JsonBackReference annotation is used to prevent infinite recursion when serializing the entities.
        * The @JoinColumn annotation is used to specify the foreign key column.
     */
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    /* The relationship of Order to Book is Many-to-Many.
        * An Order can have multiple Books.
        * A Book can be in multiple Orders.
        * The order_books table is a join table that contains the order_id and book_id columns.
        * The order_books table is used to store the many-to-many relationship between orders and books.
        * The order_id column is a foreign key that references the id column in the orders table.
        * The book_id column is a foreign key that references the id column in the books table.
        * The @JsonManagedReference annotation is used to prevent infinite recursion when serializing the entities.
        * The @JoinTable annotation is used to specify the join table and columns.
     */
    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "order_books",
            // joinColumns refers to the column(s) in the owning side (Order)
            joinColumns = @JoinColumn(name = "order_id"),
            // inverseJoinColumns refers to the column(s) in the inverse side (Book)
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;

    @Setter
    @Getter
    // The status of the order can be (BORROWED, RETURNED) using the OrderStatus enum
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Setter
    @Getter
    // The borrowed_at field is the date when the order was borrowed
    @Column(name = "borrowed_at", nullable = false, updatable = false)
    // A @Temporal annotation is used to specify the type of the date field
    // The date field is stored as a TIMESTAMP in the database
    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowed_at;

    // The @PrePersist annotation is used to specify a callback method
    // that is called before the entity is persisted
    // This is to avoid errors when the borrowed_at field is not set
    @PrePersist
    protected void onCreate() {
        borrowed_at = new Date();
        // The status is set to BORROWED when the order is created
        status = OrderStatus.BORROWED;

        // Calendar is used to add 7 days to the borrowed date
        Calendar cal = Calendar.getInstance();
        // Set the calendar to the borrowed date
        cal.setTime(borrowed_at);
        // Add 7 days to the borrowed date
        cal.add(Calendar.DAY_OF_MONTH, 7); // 7 days borrowing period
        // Set the due date to 7 days after the borrowed date
        due_date = cal.getTime();
    }

    @Setter
    @Getter
    private Date due_date;

    @Getter
    private Date returned_at;

    @Setter
    @Getter
    @Column(name = "is_picked_up", nullable = false)
    private boolean isPickedUp = false;

    /**
     * Sets the borrowed date of the Order entity and updates the status to BORROWED.
     *
     * @param borrowed_at the borrowed date to be set.
     */
    public void setBorrowed_at(Date borrowed_at) {
        this.borrowed_at = borrowed_at;

        // If the borrowed date is set, update the status to BORROWED
        if (borrowed_at != null) {
            this.status = OrderStatus.BORROWED;
        }
    }

    /**
     * Sets the returned date of the Order entity and updates the status to RETURNED.
     *
     * @param returned_at the returned date to be set.
     */
    public void setReturned_at(Date returned_at) {
        this.returned_at = returned_at;

        // If the returned date is set, update the status to RETURNED
        if (returned_at != null) {
            this.status = OrderStatus.RETURNED;
        }
    }

    /**
     * Checks if the Order entity is overdue.
     *
     * @return true if the Order entity is overdue, false otherwise.
     */
    public boolean isOverdue() {
        // If the status is not BORROWED or the borrowed date is null, the order is not overdue
        if (status != OrderStatus.BORROWED || borrowed_at == null) {
            return false;
        }

        // Check if the current date is after the due date
        // If the current date is after the due date, the order is overdue
        return new Date().after(due_date);
    }

    /**
     * Calculates the days remaining of the order till due date
     */
    public long getDaysRemaining(){
        LocalDate dueDateLocal = new java.sql.Date(getDue_date().getTime()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.DAYS.between(currentDate, dueDateLocal);
    }

    /**
     * Marks the Order entity as picked up.
     */
    public void markAsPickedUp() {
        this.isPickedUp = true;
    }
}
