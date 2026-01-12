package com.immobilier.backend.repository;

import com.immobilier.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE (:purpose IS NULL OR UPPER(p.purpose) = UPPER(:purpose))
          AND (:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%')))
          AND (:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%')))
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
          AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms)
          AND (:available IS NULL OR p.isAvailable = :available)
        ORDER BY p.createdAt DESC
    """)
    List<Product> search(
            @Param("purpose") String purpose,
            @Param("category") String category,
            @Param("address") String address,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("bedrooms") Integer bedrooms,
            @Param("available") Boolean available
    );
}
