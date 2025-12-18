package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Category createCategory(String name, MultipartFile imageFile) throws IOException {
        Category category = new Category();
        category.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            // ✅ Uploads to Cloudinary 'categories' folder
            String imageUrl = cloudinaryService.uploadImage(imageFile, "categories");
            category.setImage(imageUrl);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, MultipartFile imageFile) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(name);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(imageFile, "categories");
            category.setImage(imageUrl);
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        // Simply delete from DB. Cloudinary cleanup is optional.
        categoryRepository.deleteById(id);
    }
}