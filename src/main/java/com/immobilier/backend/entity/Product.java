package com.immobilier.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(name = "on_promotion", nullable = false)
    private boolean onPromotion = false;

    @Column(name = "previous_price", nullable = false)
    private double previousPrice = 0;

    @Column(nullable = false, length = 10)
    private String purpose; // "RENT" | "SALE"

    @Column(length = 200)
    private String address;

    private Integer surfaceM2;
    private Integer bedrooms;
    private Integer bathrooms;

    @JsonProperty("isAvailable")
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

//    @Column(name = "on_promotion", nullable = false)
//    private boolean onPromotion = false;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image")
    private List<String> images = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
        normalize();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
        normalize();
    }

    private void normalize() {
        if (name != null) name = name.trim();
        if (category != null) category = category.trim();
        if (purpose != null) purpose = purpose.trim().toUpperCase();
        if (address != null) address = address.trim();
    }
}
