package com.it120p.librarymanagementsystem.controller;

import com.it120p.librarymanagementsystem.exception.AdminNotFoundException;
import com.it120p.librarymanagementsystem.model.Admin;
import com.it120p.librarymanagementsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/admin")
    Admin newAdmin(@RequestBody Admin newAdmin) {
        newAdmin.setCreated_at(new Date());
        return adminRepository.save(newAdmin);
    }

    @PostMapping("/admins")
    List<Admin> newAdmins(@RequestBody List<Admin> newAdmins) {
        for (Admin admin : newAdmins) {
            admin.setCreated_at(new Date());
        }
        return adminRepository.saveAll(newAdmins);
    }

    @GetMapping("/admins")
    List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("/admin/{id}")
    Admin getAdminById(@PathVariable Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

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
