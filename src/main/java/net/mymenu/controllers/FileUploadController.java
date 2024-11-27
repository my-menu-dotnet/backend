package net.mymenu.controllers;

import net.mymenu.exception.NotFoundException;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @PostMapping("/upload")
    public ResponseEntity<FileStorage> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.saveFile(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> find(@PathVariable UUID id) {
        FileStorage fileStorage = fileStorageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));

        try {
            Path path = Paths.get(FileStorageService.UPLOAD_DIR + fileStorage.getFileName());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", fileStorage.getFileType())
                    .body(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new NotFoundException("File not found");
        }
    }
}
