package net.mymenu.repository;

import net.mymenu.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserId(UUID userId);
}
