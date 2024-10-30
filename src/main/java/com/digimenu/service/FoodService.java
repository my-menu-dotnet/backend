package com.digimenu.service;

import com.digimenu.exception.NotFoundException;
import com.digimenu.models.Food;
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
    private JwtHelper jwtHelper;

    public Food findById(UUID id) {
        UUID companyId = jwtHelper.extractCompanyId();
        return foodRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Food not found"));
    }
}
