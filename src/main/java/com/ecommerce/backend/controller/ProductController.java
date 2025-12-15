package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    // ---------- GET ALL ----------
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ---------- GET ONE ----------
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ---------- CREATE ----------
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("previousPrice") double previousPrice,
            // FIX: Receive as String
            @RequestParam("isAvailable") String isAvailableStr,
            @RequestParam("onPromotion") String onPromotionStr,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam(value = "existingImages", required = false) String existingImagesJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            ensureUploadDir();

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setPreviousPrice(previousPrice);

            // FIX: Manually parse the String to boolean
            product.setAvailable(Boolean.parseBoolean(isAvailableStr));
            product.setOnPromotion(Boolean.parseBoolean(onPromotionStr));

            product.setCategory(category);
            product.setDescription(description);

            List<String> imageList = new ArrayList<>();

            if (existingImagesJson != null && !existingImagesJson.isBlank()) {
                imageList.addAll(
                        objectMapper.readValue(existingImagesJson, new TypeReference<List<String>>() {})
                );
            }

            if (images != null && !images.isEmpty()) {
                imageList.addAll(uploadFiles(images));
            }

            product.setImages(imageList);

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create product: " + e.getMessage());
        }
    }

    // ---------- UPDATE ----------
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("previousPrice") double previousPrice,
            // FIX: Receive as String
            @RequestParam("isAvailable") String isAvailableStr,
            @RequestParam("onPromotion") String onPromotionStr,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam(value = "existingImages", required = false) String existingImagesJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImages
    ) {
        try {
            ensureUploadDir();

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setName(name);
            product.setPrice(price);
            product.setPreviousPrice(previousPrice);

            // FIX: Manually parse the String to boolean
            product.setAvailable(Boolean.parseBoolean(isAvailableStr));
            product.setOnPromotion(Boolean.parseBoolean(onPromotionStr));

            product.setCategory(category);
            product.setDescription(description);

            List<String> images = new ArrayList<>();

            if (existingImagesJson != null && !existingImagesJson.isBlank()) {
                images.addAll(objectMapper.readValue(existingImagesJson, new TypeReference<List<String>>() {}));
            }

            if (newImages != null && !newImages.isEmpty()) {
                images.addAll(uploadFiles(newImages));
            }

            product.setImages(images);

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }

    // ---------- BATCH IMAGE UPLOAD ----------
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadBatch(@RequestParam("images") List<MultipartFile> images) {
        try {
            ensureUploadDir();
            List<String> uploadedUrls = uploadFiles(images);
            return ResponseEntity.ok(uploadedUrls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload images: " + e.getMessage());
        }
    }

    // ---------- HELPERS ----------
    private void ensureUploadDir() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
    }

    private List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String original = file.getOriginalFilename()
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^a-zA-Z0-9_.-]", "");
            String filename = UUID.randomUUID() + "_" + original;

            File dest = new File(UPLOAD_DIR + filename);
            file.transferTo(dest);

            imageUrls.add("/uploads/" + filename);
        }

        return imageUrls;
    }
}