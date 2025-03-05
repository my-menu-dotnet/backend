package net.mymenu.controllers;

import net.mymenu.dto.auth.AuthVerifyEmail;
import net.mymenu.dto.auth.AuthLogin;
import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.dto.auth.GoogleTokenDTO;
import net.mymenu.enums.auth.EmailCodeType;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.auth.RefreshToken;
import net.mymenu.models.User;
import net.mymenu.repository.auth.RefreshTokenRepository;
import net.mymenu.repository.UserRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.AuthService;
import jakarta.validation.Valid;
import net.mymenu.service.CookieService;
import net.mymenu.service.EmailCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/oauth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CookieService cookieService;

    @PostMapping("/google")
    public ResponseEntity<?> loginOAuthGoogle(@Valid @RequestBody GoogleTokenDTO googleTokenDTO) {
        User user = authService.loginOAuthGoogle(googleTokenDTO);
        return authService.getNewTokensResponseEntity(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(() -> new NotFoundException("Refresh token not found"));

            refreshTokenRepository.delete(refreshTokenEntity);

            String email = jwtHelper.extractRefreshTokenEmail(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new SecurityException("User not found"));

            return authService.getNewTokensResponseEntity(user);
        } catch (AccountStatusException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", defaultValue = "null") String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .ifPresent(refreshTokenEntity -> refreshTokenRepository.delete(refreshTokenEntity));

        ResponseCookie cookieRefreshToken = cookieService.createCookie("refreshToken", "", 0, "/v1/oauth/refresh-token");
        ResponseCookie cookieAccessToken = cookieService.createCookie("accessToken", "", 0, "/");
        ResponseCookie cookieIsAuthenticated = cookieService.createIsAuthenticatedCookie(false);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString(), cookieIsAuthenticated.toString())
                .build();
    }
}
