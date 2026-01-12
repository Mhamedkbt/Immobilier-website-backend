package com.immobilier.backend.config;

import com.immobilier.backend.entity.Admin;
import com.immobilier.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String username;

    @Value("${app.admin.password:admin}")
    private String password;

    public AdminSeeder(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.findByUsername(username).isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername(username);

            // ✅ store hashed password (secure)
            admin.setPassword(passwordEncoder.encode(password));

            adminRepository.save(admin);
            System.out.println("✅ Admin created: " + username);
        } else {
            System.out.println("✅ Admin already exists: " + username);
        }
    }
}
