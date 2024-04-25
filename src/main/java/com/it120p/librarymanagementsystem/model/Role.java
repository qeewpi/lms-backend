package com.it120p.librarymanagementsystem.model;

import jakarta.persistence.*;

/**
 * The Role class is an entity model object for a Role.
 * It contains fields for the role's ID and name.
 * The ID is generated automatically when a Role entity is created.
 * The name is an enum of type ERole.
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** The name of the role.
     *  It takes a value from the ERole enum and
     *  is stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     * Default constructor for the Role entity.
     */
    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
