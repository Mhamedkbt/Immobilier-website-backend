package com.immobilier.backend.config;

import io.imagekit.sdk.ImageKit;
// REMOVED: import io.imagekit.sdk.config.Configuration; <--- This was the cause of the error
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // This is the @Configuration annotation

@Configuration
public class ImageKitConfig {

    @Value("${imagekit.public-key}")
    private String publicKey;

    @Value("${imagekit.private-key}")
    private String privateKey;

    @Value("${imagekit.url-endpoint}")
    private String urlEndpoint;

    @Bean
    public ImageKit imageKit() {
        if (publicKey == null || privateKey == null || urlEndpoint == null) {
            throw new RuntimeException("Missing ImageKit keys!");
        }

        // We use the full package name here to avoid the name clash
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration(
                publicKey,
                privateKey,
                urlEndpoint
        );

        ImageKit imageKit = ImageKit.getInstance();
        imageKit.setConfig(config);

        return imageKit;
    }
}