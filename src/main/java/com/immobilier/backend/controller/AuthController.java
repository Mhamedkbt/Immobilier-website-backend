package com.immobilier.backend.controller;

import com.immobilier.backend.config.JwtUtil;
import com.immobilier.backend.dto.LoginRequest;
import com.immobilier.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminService adminService;

    @Autowired
    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean authenticated = adminService.checkLogin(
                request.getUsername(),
                request.getPassword()
        );

        if (authenticated) {
            String token = jwtUtil.generateToken(request.getUsername()); // ✅ ONE PARAM ONLY
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}
