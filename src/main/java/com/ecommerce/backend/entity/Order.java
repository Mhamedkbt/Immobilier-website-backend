package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;

    // ✅ NEW FIELD
    private String city;

    private String status; // Pending, Confirmed, Delivered
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> products;
}
