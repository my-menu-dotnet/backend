package net.mymenu.file_storage;

import jakarta.annotation.PostConstruct;
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
