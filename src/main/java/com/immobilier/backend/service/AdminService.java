package com.immobilier.backend.service;

import com.immobilier.backend.entity.Admin;
import java.util.Optional;

public interface AdminService {

    Admin saveAdmin(Admin admin);

    Optional<Admin> getAdminById(Long id);

    Optional<Admin> getAdminByUsername(String username);

    // Validate if a given password matches the admin's password
    boolean validatePassword(Admin admin, String password);

    // Check login credentials
    boolean checkLogin(String username, String password);
}
