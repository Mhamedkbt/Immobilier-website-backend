package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Admin;
import java.util.Optional;

public interface AdminService {

    Admin saveAdmin(Admin admin);                 // Save a new admin
    Optional<Admin> getAdminById(Long id);       // Get admin by ID
    Optional<Admin> getAdminByUsername(String username); // Get admin by username

    // New method for login
    boolean checkLogin(String username, String password);
}
