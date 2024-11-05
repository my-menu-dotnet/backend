package com.digimenu.controllers;

import com.digimenu.models.FileStorage;
import com.digimenu.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<FileStorage> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.saveFile(file));
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<byte[]> find(@PathVariable String imageName) {
        return ResponseEntity.ok(fileStorageService.findBytesByImageName(imageName));
    }
}
