package net.mymenu.config;

import jakarta.annotation.PostConstruct;
import net.mymenu.models.FileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${file.url}")
    private String fileUrl;

    @PostConstruct
    public void init() {
        FileStorage.setFileUrl(fileUrl);
    }
}
