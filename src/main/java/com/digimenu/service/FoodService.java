package com.digimenu.service;

import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Food;
import com.digimenu.repository.CategoryRepository;
import com.digimenu.repository.FileStorageRepository;
import com.digimenu.repository.FoodRepository;
import com.digimenu.security.JwtHelper;
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
