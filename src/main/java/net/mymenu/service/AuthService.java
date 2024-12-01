package net.mymenu.service;

import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.exception.DuplicateException;
import net.mymenu.exception.EmailCodeExpiredException;
import net.mymenu.exception.EmailCodeInvalidException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.RefreshToken;
import net.mymenu.models.User;
import net.mymenu.models.auth.EmailCode;
import net.mymenu.repository.RefreshTokenRepository;
import net.mymenu.repository.UserRepository;
import net.mymenu.repository.auth.EmailCodeRepository;
import net.mymenu.security.JwtHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private EmailCodeRepository emailCodeRepository;

    public User registerUser(AuthRegister authRegister) {
        userRepository.findByEmailOrCpf(authRegister.getEmail(), authRegister.getCpf())
                .ifPresent(_ -> {
                    throw new DuplicateException("Email or CPF already registered");
                });

        User user = User.builder()
                .name(authRegister.getName())
                .email(authRegister.getEmail())
                .cpf(authRegister.getCpf())
                .phone(authRegister.getPhone())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .build();

        userRepository.save(user);

        return user;
    }

    @NotNull
    public ResponseEntity<User> getResponseEntity(User user, String jwt, String newRefreshToken, HttpStatus status) {
        RefreshToken newRefreshTokenEntity = new RefreshToken(newRefreshToken, user);
        refreshTokenRepository.save(newRefreshTokenEntity);

        ResponseCookie cookieRefreshToken = createRefreshTokenCookie(newRefreshToken);
        ResponseCookie cookieAccessToken = createAccessTokenCookie(jwt);

        return ResponseEntity
                .status(status)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .body(user);
    }

    public ResponseEntity<User> getResponseEntity(User user, String jwt, String newRefreshToken) {
        return getResponseEntity(user, jwt, newRefreshToken, HttpStatus.OK);
    }

    public ResponseCookie createAccessTokenCookie(String jwt) {
        return createCookie("accessToken", jwt, jwtHelper.ACCESS_EXPIRY_IN_SECONDS, "/");
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return createCookie("refreshToken", refreshToken, jwtHelper.REFRESH_EXPIRY_IN_SECONDS, "/auth");
    }

    public ResponseCookie createCookie(String name, String value, int maxAge, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }

    public boolean validateEmailCode(User user, String code) {
        EmailCode emailCode = emailCodeRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Email code not found"))
                .stream()
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(EmailCodeInvalidException::new);

        emailCodeRepository.delete(emailCode);

        LocalDateTime now = LocalDateTime.now();

        if (emailCode.getCreatedAt().plusMinutes(5).isBefore(now)) {
            throw new EmailCodeExpiredException();
        }

        return true;
    }

    public EmailCode createEmailCode(User user) {
        EmailCode emailCode = EmailCode.builder()
                .code(generateRandomCode())
                .userId(user.getId())
                .build();

        emailCodeRepository.save(emailCode);

        return emailCode;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
