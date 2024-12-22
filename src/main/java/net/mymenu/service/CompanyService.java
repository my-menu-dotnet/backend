package net.mymenu.service;

import net.mymenu.dto.AddressRequest;
import net.mymenu.models.Address;
import net.mymenu.models.Category;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

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
