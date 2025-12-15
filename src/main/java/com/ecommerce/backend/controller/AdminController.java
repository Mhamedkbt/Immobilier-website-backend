package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.Admin;
import com.ecommerce.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Get admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        return admin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get admin by username
    @GetMapping("/username/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username) {
        Optional<Admin> admin = adminService.getAdminByUsername(username);
        return admin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update admin (username / password) with old password verification
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable Long id,
            @RequestBody Admin updatedAdmin,
            @RequestParam(required = false) String oldPassword
    ) {
        Optional<Admin> adminOpt = adminService.getAdminById(id);

        if (adminOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Admin admin = adminOpt.get();

        // Update username if provided
        if (updatedAdmin.getUsername() != null && !updatedAdmin.getUsername().isBlank()) {
            admin.setUsername(updatedAdmin.getUsername());
        }

        // Update password if provided
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isBlank()) {
            if (oldPassword == null || oldPassword.isBlank()) {
                return ResponseEntity.badRequest().body("Old password is required to update the password");
            }
            boolean correct = adminService.validatePassword(admin, oldPassword);
            if (!correct) {
                return ResponseEntity.status(401).body("Old password is incorrect");
            }
            admin.setPassword(updatedAdmin.getPassword());
        }

        Admin savedAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }
}
