package com.it120p.librarymanagementsystem.model;

/**
 * The OrderStatus enum represents the different statuses an Order can have in the system.
 * Currently, there are three statuses: BORROWED, OVERDUE, and RETURNED.
 */
public enum OrderStatus {
    /**
     * The BORROWED status represents an Order that has been borrowed by a User.
     */
    BORROWED,

    /**
     * The OVERDUE status represents an Order that has not been returned by the User within the due date.
     */
    OVERDUE,

    /**
     * The RETURNED status represents an Order that has been returned by the User.
     */
    RETURNED
}
