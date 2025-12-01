package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Admin;
import com.ecommerce.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
    public boolean checkLogin(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        return admin.map(a -> a.getPassword().equals(password)).orElse(false);
    }

}



