package net.mymenu.controllers;

import net.mymenu.dto.auth.AuthLogin;
import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.exception.InternalErrorException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.RefreshToken;
import net.mymenu.models.User;
import net.mymenu.repository.RefreshTokenRepository;
import net.mymenu.repository.UserRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.AuthService;
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
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", defaultValue = "null") String refreshToken) {
        if (refreshToken.equals("null")) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        refreshTokenRepository.findById(refreshToken)
                .ifPresent(refreshTokenEntity -> refreshTokenRepository.delete(refreshTokenEntity));

        ResponseCookie cookieRefreshToken = authService.createCookie("refreshToken", "", 0, "/auth");
        ResponseCookie cookieAccessToken = authService.createCookie("accessToken", "", 0, "/");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<?> check() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
