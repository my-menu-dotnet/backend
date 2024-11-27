package net.mymenu.service;

import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Food;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import net.mymenu.repository.FoodRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Deprecated
    public Food[] findAll() {
        return foodRepository.findAll()
                .toArray(new Food[0]);
    }

    public Food findById(UUID id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }
}
