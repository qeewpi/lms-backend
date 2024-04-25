package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository interface is a Spring Data JPA repository for User entities.
 *
 * In this case, the repository is for User entities, and the ID of the User entity is of type Long.
 *
 * The findByUsername method is a query method that finds a user by its username.
 * The existsByUsername method is a query method that checks if a user exists by its username.
 * The existsByEmail method is a query method that checks if a user exists by its email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by its username.
     *
     * @param username the username of the user to be searched.
     * @return an Optional of User that contains the user if found, or an empty Optional if not.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by its username.
     *
     * @param username the username of the user to be checked.
     * @return a boolean value that indicates if the user exists by its username.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user exists by its email.
     *
     * @param email the email of the user to be checked.
     * @return a Boolean that is true if the user exists, or false if not.
     */
    Boolean existsByEmail(String email);
}
