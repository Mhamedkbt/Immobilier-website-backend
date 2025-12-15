package com.ecommerce.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 1. Always allow pre-flight OPTIONS requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Allow public access for login/registration
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. Allow public access to uploaded images (Necessary for frontend display)
                        .requestMatchers("/uploads/**").permitAll()

                        // --- 🎯 CRITICAL FIX START: Open GET requests for public viewing ---

                        // FIX A: Allow GET requests to /products for public Home Page
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // FIX B (The new one): Allow GET requests to /categories for public Home Page
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 5. ✅ ALLOW POST /api/orders FOR EVERYONE
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()


                        // and ALL actions on /orders
                        .requestMatchers("/api/products/**", "/api/orders/**", "/api/categories/**").hasAuthority("ROLE_ADMIN")

                        // 5. Authenticate any other request not explicitly permitted above
                        .anyRequest().authenticated()
                )

                // Use the standard filter placement
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Maintain the security context settings you provided
                .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
                .requestCache((requestCache) -> requestCache.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}