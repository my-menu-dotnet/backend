package net.mymenu.repository.auth;

import net.mymenu.models.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserId(UUID userId);
}
