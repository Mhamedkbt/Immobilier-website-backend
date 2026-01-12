package com.immobilier.backend.controller;

import com.immobilier.backend.entity.Category;
import com.immobilier.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ GET ALL CATEGORIES
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ CREATE CATEGORY (JSON)
    // Frontend sends { "name": "...", "image": "https://..." }
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // ✅ UPDATE CATEGORY (JSON)
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));

        category.setName(categoryDetails.getName());
        category.setImage(categoryDetails.getImage());

        return categoryRepository.save(category);
    }

    // ✅ DELETE CATEGORY (safe)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
