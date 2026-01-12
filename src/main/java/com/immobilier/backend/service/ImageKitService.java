package com.immobilier.backend.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageKitService {

    @Autowired
    private ImageKit imageKit;

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        try {
            // Prepare the request with the file bytes and name
            FileCreateRequest request = new FileCreateRequest(file.getBytes(), file.getOriginalFilename());
            request.setFolder(folder); // Sets the folder in ImageKit
            request.setUseUniqueFileName(true);

            // Execute upload
            Result result = imageKit.upload(request);

            // Return the direct URL of the uploaded image
            return result.getUrl();
        } catch (Exception e) {
            throw new IOException("ImageKit upload failed: " + e.getMessage());
        }
    }
}