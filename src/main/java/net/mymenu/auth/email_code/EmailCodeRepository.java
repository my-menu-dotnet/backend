package net.mymenu.auth.email_code;

import net.mymenu.auth.enums.EmailCodeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Integer> {
    Optional<EmailCode> findAllByEmailAndTypeAndCode(String email, EmailCodeType type, String code);

    List<EmailCode> findAllByEmailAndTypeOrderByCreatedAtDesc(String email, EmailCodeType type);
}
