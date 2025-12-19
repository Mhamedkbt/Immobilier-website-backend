package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty; // 👈 REQUIRED IMPORT

import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;
    private double previousPrice;

    // FIX: Force Jackson to use "isAvailable" as the JSON key,
    // instead of the default "available", matching the frontend code.
    @JsonProperty("isAvailable")
    private boolean isAvailable;

    private boolean onPromotion;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> images; // store URLs
}