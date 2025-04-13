package net.mymenu.company;

import net.mymenu.address.dto.AddressRequest;
import net.mymenu.address.Address;
import net.mymenu.category.Category;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.category.CategoryRepository;
import net.mymenu.file_storage.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public List<Category> findCategories(List<UUID> categoryIds) {
        return categoryRepository.findAllByIdList(categoryIds)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public FileStorage findImage(UUID imageId) {
        return fileStorageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public Address buildAddress(AddressRequest addressRequest) {
        return Address.builder()
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .complement(addressRequest.getComplement())
                .neighborhood(addressRequest.getNeighborhood())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .build();
    }
}
