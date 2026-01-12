package com.immobilier.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Prevent duplicates like "Apartment" and " apartment "
    @Column(nullable = false, unique = true, length = 80)
    private String name;

    // ImageKit/Cloudinary URL
    @Column(length = 500)
    private String image;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim();
    }
}
