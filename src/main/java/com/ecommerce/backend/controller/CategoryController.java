// src/main/java/com/ecommerce/backend/controller/CategoryController.java

package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.service.CategoryService; // 🎯 NEW IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // 🎯 NEW IMPORT
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired // 🎯 NEW: Inject the service
    private CategoryService categoryService;

    // --- GET (UNCHANGED) ---
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // --- POST (MODIFIED to handle MultipartFile) ---
    // Uses @RequestParam instead of @RequestBody because it handles files
    @PostMapping
    public Category createCategory(
            @RequestParam("name") String name, // Category name
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile // Optional file upload
    ) throws IOException {
        return categoryService.createCategory(name, imageFile);
    }

    // --- PUT (MODIFIED to handle MultipartFile) ---
    @PutMapping("/{id}")
    public Category updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name, // Updated name
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile // Optional new file
    ) throws IOException {
        // NOTE: If the user only changes the name, the imageFile will be null or empty, 
        // and the service will handle keeping the old image.
        return categoryService.updateCategory(id, name, imageFile);
    }

    // --- DELETE (MODIFIED to use Service for file cleanup) ---
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}