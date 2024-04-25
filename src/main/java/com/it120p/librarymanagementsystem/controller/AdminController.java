package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.AdminNotFoundException;
import com.it120p.librarymanagementsystem.model.Admin;
import com.it120p.librarymanagementsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * The AdminController class manages the CRUD operations for Admin entities.
 * It uses the AdminRepository to interact with the database.
 * All methods in this class require the user to have the 'ADMIN' role.
 */
@RestController
@CrossOrigin("http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Creates a new Admin entity and saves it to the database.
     * The creation date is set to the current date.
     *
     * @param newAdmin the Admin entity to be created.
     * @return the created Admin entity.
     */
    @PostMapping("/admin")
    Admin newAdmin(@RequestBody Admin newAdmin) {
        newAdmin.setCreated_at(new Date());
        return adminRepository.save(newAdmin);
    }

    /**
     * This is the same as the previous method,
     * but it creates multiple Admin entities at once
     *
     * @param newAdmins the list of Admin entities to be created.
     * @return the list of created Admin entities.
     */
    @PostMapping("/admins")
    List<Admin> newAdmins(@RequestBody List<Admin> newAdmins) {
        for (Admin admin : newAdmins) {
            admin.setCreated_at(new Date());
        }
        return adminRepository.saveAll(newAdmins);
    }

    /**
     * Retrieves all Admin entities from the database.
     *
     * @return the list of all Admin entities.
     */
    @GetMapping("/admins")
    List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /**
     * Retrieves a single Admin entity from the database by its ID.
     *
     * @param id the ID of the Admin entity.
     * @return the Admin entity with the specified ID.
     * @throws AdminNotFoundException if no Admin entity with the specified ID is found.
     */
    @GetMapping("/admin/{id}")
    Admin getAdminById(@PathVariable Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

    /**
     * Updates an existing Admin entity in the database.
     *
     * @param newAdmin the new Admin entity data.
     * @param id the ID of the Admin entity.
     * @return the updated Admin entity.
     * @throws AdminNotFoundException if no Admin entity with the specified ID is found.
     */
    @PutMapping("/admin/{id}")
    Admin updateAdmin(@RequestBody Admin newAdmin, @PathVariable Long id) {
        return adminRepository.findById(id)
                .map(admin -> {
                    admin.setUsername(newAdmin.getUsername());
                    admin.setName(newAdmin.getName());
                    admin.setPassword(newAdmin.getPassword());
                    return adminRepository.save(admin);
                })
                .orElseThrow(() -> new AdminNotFoundException(id));
    }

    /**
     * Deletes an existing Admin entity from the database.
     *
     * @param id the ID of the Admin entity.
     * @return a message confirming the deletion.
     * @throws AdminNotFoundException if no Admin entity with the specified ID is found.
     */
    @DeleteMapping("/admin/{id}")
    String deleteAdmin(@PathVariable Long id) {
        if(adminRepository.existsById(id)){
            adminRepository.deleteById(id);
            return "Admin with ID: " + id + " has been deleted.";
        } else {
            throw new AdminNotFoundException(id);
        }
    }
}
