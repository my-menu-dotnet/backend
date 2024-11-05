package com.digimenu.service;

import com.digimenu.exception.InternalErrorException;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Company;
import com.digimenu.models.FileStorage;
import com.digimenu.models.User;
import com.digimenu.repository.CompanyRepository;
import com.digimenu.repository.FileStorageRepository;
import com.digimenu.security.JwtHelper;
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

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtHelper jwtHelper;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public FileStorage saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NotFoundException("File is empty");
        }

        UUID companyId = jwtHelper.extractCompanyId();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        User user = jwtHelper.extractUser();

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

            return createFile(new FileStorage(
                    fileName,
                    file.getContentType(),
                    file.getSize(),
                    null,
                    company,
                    user
            ));
        } catch (IOException e) {
            throw new InternalErrorException("Failed to save file");
        }
    }

    public byte[] findBytesByImageName(String imageName) {
        try {
            Path path = Paths.get(UPLOAD_DIR + imageName);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new NotFoundException("File not found");
        }
    }

    public FileStorage createFile(FileStorage fileStorage) {
        return fileStorageRepository.save(fileStorage);
    }
}
