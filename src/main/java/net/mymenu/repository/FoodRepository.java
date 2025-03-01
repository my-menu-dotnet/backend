package net.mymenu.repository;

import net.mymenu.models.Company;
import net.mymenu.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> { }
