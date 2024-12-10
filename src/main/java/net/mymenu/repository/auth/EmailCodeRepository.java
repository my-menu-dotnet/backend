package net.mymenu.repository.auth;

import net.mymenu.enums.auth.EmailCodeType;
import net.mymenu.models.auth.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Integer> {
    Optional<List<EmailCode>> findAllByUserIdAndType(UUID userId, EmailCodeType type);

    Optional<List<EmailCode>> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, EmailCodeType type);
}
