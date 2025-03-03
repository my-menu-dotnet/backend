package net.mymenu.repository.auth;

import net.mymenu.enums.auth.EmailCodeType;
import net.mymenu.models.auth.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Integer> {
    Optional<EmailCode> findAllByEmailAndTypeAndCode(String email, EmailCodeType type, String code);

    List<EmailCode> findAllByEmailAndTypeOrderByCreatedAtDesc(String email, EmailCodeType type);
}
