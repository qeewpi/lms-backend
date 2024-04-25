package com.it120p.librarymanagementsystem.repository;

import com.it120p.librarymanagementsystem.model.ERole;
import com.it120p.librarymanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The RoleRepository interface is a Spring Data JPA repository for Role entities.
 *
 * In this case, the repository is for Role entities, and the ID of the Role entity is of type Integer.
 *
 * The findByName method is a query method that finds a role by its name.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to be searched.
     * @return an Optional of Role that contains the role if found, or an empty Optional if not.
     */
    Optional<Role> findByName(ERole name);
}
