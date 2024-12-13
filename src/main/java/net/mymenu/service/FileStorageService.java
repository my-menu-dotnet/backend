package net.mymenu.service;

import net.mymenu.exception.InternalErrorException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    public static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public FileStorage saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty");
        }

        try {
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null) {
                throw new NotFoundException("File name not found");
            }

            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String fileName = UUID.randomUUID() + fileExtension;

            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());

            file.transferTo(path);

            return fileStorageRepository.save(
                    FileStorage.builder()
                            .fileName(fileName)
                            .fileType(file.getContentType())
                            .size(file.getSize())
                            .build()
            );
        } catch (IOException e) {
            throw new InternalErrorException("Failed to save file");
        }
    }
}
