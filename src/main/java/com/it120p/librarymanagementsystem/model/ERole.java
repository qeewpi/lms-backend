package com.it120p.librarymanagementsystem.model;

/**
 * The ERole enum represents the different roles a user can have in the system.
 * Currently, there are two roles: USER and ADMIN.
 */
public enum ERole {
    /**
     * Represents a standard user in the system. Users with this role have limited access rights.
     */
    ROLE_USER,

    /**
     * Represents an administrator in the system. Users with this role have full access rights.
     */
    ROLE_ADMIN
}
