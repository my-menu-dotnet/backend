package net.mymenu.service;

import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.exception.*;
import net.mymenu.models.auth.RefreshToken;
import net.mymenu.models.User;
import net.mymenu.repository.auth.RefreshTokenRepository;
import net.mymenu.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CookieService cookieService;

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
        RefreshToken newRefreshTokenEntity = RefreshToken
                .builder()
                .token(newRefreshToken)
                .userId(user.getId())
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        ResponseCookie cookieRefreshToken = cookieService.createRefreshTokenCookie(newRefreshToken);
        ResponseCookie cookieAccessToken = cookieService.createAccessTokenCookie(jwt);

        return ResponseEntity
                .status(status)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .body(user);
    }

    public ResponseEntity<User> getResponseEntity(User user, String jwt, String newRefreshToken) {
        return getResponseEntity(user, jwt, newRefreshToken, HttpStatus.OK);
    }
}
