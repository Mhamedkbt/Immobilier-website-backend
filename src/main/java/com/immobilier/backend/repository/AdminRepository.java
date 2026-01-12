package com.immobilier.backend.repository;

import com.immobilier.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Custom method to find admin by username
    Optional<Admin> findByUsername(String username);
}
