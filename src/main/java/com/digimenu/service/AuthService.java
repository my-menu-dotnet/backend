package com.digimenu.service;

import com.digimenu.dto.auth.AuthRegister;
import com.digimenu.exception.DuplicateException;
import com.digimenu.models.RefreshToken;
import com.digimenu.models.User;
import com.digimenu.repository.RefreshTokenRepository;
import com.digimenu.repository.UserRepository;
import com.digimenu.security.JwtHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtHelper jwtHelper;

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
    public ResponseEntity<?> getResponseEntity(User user, String jwt, String newRefreshToken) {
        RefreshToken newRefreshTokenEntity = new RefreshToken(newRefreshToken, user);
        refreshTokenRepository.save(newRefreshTokenEntity);

        ResponseCookie cookieRefreshToken = createRefreshTokenCookie(newRefreshToken);
        ResponseCookie cookieAccessToken = createAccessTokenCookie(jwt);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .build();
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
}
