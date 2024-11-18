package com.digimenu.repository;

import com.digimenu.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query("SELECT a FROM Address a WHERE a.zipCode = :zipCode AND a.latitude IS NOT NULL AND a.longitude IS NOT NULL ORDER BY a.createdAt LIMIT 1")
    Optional<Address> findCoordsByZipCode(String zipCode);
}
