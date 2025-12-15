// src/main/java/com/ecommerce/backend/service/CategoryService.java

package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 🎯 IMPORTANT: Define your base upload directory
    // Ensure this directory exists on your server.
    private final String UPLOAD_DIR = "uploads/categories/";

    /**
     * Saves a category image file and returns its relative path.
     */
    private String saveImageFile(MultipartFile file) throws IOException {
        // Create the directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique filename to prevent conflicts
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(uniqueFileName);

        // Save the file to the local file system
        Files.copy(file.getInputStream(), filePath);

        // Return the relative path that will be stored in the database
        return UPLOAD_DIR + uniqueFileName;
    }

    /**
     * Creates a new category, handling image upload if a file is provided.
     */
    public Category createCategory(String name, MultipartFile imageFile) throws IOException {
        Category category = new Category();
        category.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImageFile(imageFile);
            category.setImage(imagePath);
        }

        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category, handling name change and optional image upload.
     */
    public Category updateCategory(Long id, String name, MultipartFile imageFile) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(name);

        // If a new file is uploaded, save it and update the image path
        if (imageFile != null && !imageFile.isEmpty()) {

            // Optional: Delete old image file before saving new one (for cleanup)
            if (category.getImage() != null) {
                try {
                    Files.deleteIfExists(Paths.get(category.getImage()));
                } catch (Exception e) {
                    System.err.println("Could not delete old image: " + category.getImage());
                }
            }

            String imagePath = saveImageFile(imageFile);
            category.setImage(imagePath);
        }

        return categoryRepository.save(category);
    }

    // You can also add a utility method to delete the image file when the category is deleted
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Delete the image file from the server
        if (category.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get(category.getImage()));
            } catch (IOException e) {
                System.err.println("Could not delete image file: " + category.getImage() + " Error: " + e.getMessage());
            }
        }

        // Delete the category from the database
        categoryRepository.delete(category);
    }
}