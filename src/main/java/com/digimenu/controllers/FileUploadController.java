package com.digimenu.controllers;

import com.digimenu.models.FileStorage;
import com.digimenu.models.User;
import com.digimenu.security.JwtHelper;
import com.digimenu.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<FileStorage> upload(@RequestParam("file") MultipartFile file) {
        User user = jwtHelper.extractUser();

        if (user.getCompanies().isEmpty()) {
            throw new SecurityException("User does not have any companies");
        }

        return ResponseEntity.ok(fileStorageService.saveFile(file));
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> find(@PathVariable String fileName) {
        return ResponseEntity.ok(fileStorageService.findBytesByImageName(fileName));
    }
}
