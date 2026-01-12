package com.immobilier.backend.service;

import com.immobilier.backend.entity.Admin;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.immobilier.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public boolean validatePassword(Admin admin, String password) {
        if (admin == null || password == null) return false;
        return passwordEncoder.matches(password, admin.getPassword());
    }


    @Override
    public boolean checkLogin(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        return admin.map(a -> validatePassword(a, password)).orElse(false);
    }
}
