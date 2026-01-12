package com.immobilier.backend.controller;

import com.immobilier.backend.entity.Product;
import com.immobilier.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // ✅ GET ALL PRODUCTS (and allow simple filters for immo)
    // Examples:
    // /api/products?purpose=RENT
    // /api/products?purpose=SALE&category=Apartment
    // /api/products?address=casablanca
    // /api/products?minPrice=3000&maxPrice=9000
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) String purpose,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) Boolean available
    ) {

        if (purpose != null) purpose = purpose.trim().toUpperCase();

        // If no filters => keep old behavior exactly
        boolean noFilters =
                purpose == null && category == null && address == null &&
                        minPrice == null && maxPrice == null && bedrooms == null && available == null;

        if (noFilters) {
            return productRepository.findAll();
        }

        // Minimal filter logic (no breaking changes)
        // If you want cleaner filtering later, we can switch to Specifications.
        return productRepository.search(
                purpose, category, address,
                minPrice, maxPrice, bedrooms, available
        );
    }

    // ✅ GET ONE PRODUCT BY ID (needed for property details page)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }

    // ✅ CREATE PRODUCT (JSON ONLY)
    @PostMapping
    @Transactional
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // ✅ UPDATE PRODUCT (JSON ONLY) - now includes new immo fields
    @PutMapping("/{id}")
    @Transactional
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updated) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));

        // keep what you already had
        product.setName(updated.getName());
        product.setPrice(updated.getPrice());
//        product.setPreviousPrice(updated.getPreviousPrice());
        product.setAvailable(updated.isAvailable());
//        product.setOnPromotion(updated.isOnPromotion());
        product.setCategory(updated.getCategory());
        product.setDescription(updated.getDescription());
        product.setImages(updated.getImages());

        // ✅ NEW immo fields (this is the main missing part)
        product.setPurpose(updated.getPurpose());
        product.setAddress(updated.getAddress());
        product.setSurfaceM2(updated.getSurfaceM2());
        product.setBedrooms(updated.getBedrooms());
        product.setBathrooms(updated.getBathrooms());

        return productRepository.save(product);
    }

    // ✅ DELETE PRODUCT
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
}
