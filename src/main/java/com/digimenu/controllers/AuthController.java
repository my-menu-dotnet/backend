package com.digimenu.controllers;

import com.digimenu.dto.auth.AuthLogin;
import com.digimenu.dto.auth.AuthRegister;
import com.digimenu.exception.InternalErrorException;
import com.digimenu.exception.NotFoundException;
import com.digimenu.models.RefreshToken;
import com.digimenu.models.User;
import com.digimenu.repository.RefreshTokenRepository;
import com.digimenu.repository.UserRepository;
import com.digimenu.security.JwtHelper;
import com.digimenu.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLogin authLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authLogin.getEmail(), authLogin.getPassword())
        );

        final User user = userRepository.findByEmail(authLogin.getEmail())
                .orElseThrow(() -> new InternalErrorException("User authenticated but not found"));

        final String jwt = jwtHelper.generateUserToken(user);
        final String refreshToken = jwtHelper.generateRefreshToken(user);

        return authService.getResponseEntity(user, jwt, refreshToken);
    }

    @PostMapping("/login/anonymous")
    public ResponseEntity<?> loginAnonymous() {
        String jwt = jwtHelper.generateAnonymousToken();
        ResponseCookie cookieAccessToken = authService.createAccessTokenCookie(jwt);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieAccessToken.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        User user = authService.registerUser(authRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        refreshTokenRepository.delete(refreshTokenEntity);

        String email = jwtHelper.extractRefreshTokenEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InternalErrorException("User not found"));

        String jwt = jwtHelper.generateUserToken(user);
        String newRefreshToken = jwtHelper.generateRefreshToken(user);

        return authService.getResponseEntity(user, jwt, newRefreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        refreshTokenRepository.delete(refreshTokenEntity);

        ResponseCookie cookieRefreshToken = authService.createCookie("refreshToken", "", 0, "/auth/refresh-token");
        ResponseCookie cookieAccessToken = authService.createCookie("accessToken", "", 0, "/");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .build();
    }
}
