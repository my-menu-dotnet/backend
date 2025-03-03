package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.mymenu.dto.auth.AuthLogin;
import net.mymenu.dto.auth.AuthSimplifiedEmail;
import net.mymenu.dto.auth.AuthSimplifiedLogin;
import net.mymenu.models.User;
import net.mymenu.models.auth.RefreshToken;
import net.mymenu.repository.UserRepository;
import net.mymenu.repository.auth.RefreshTokenRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.AuthService;
import net.mymenu.service.CookieService;
import net.mymenu.service.EmailCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/simplified")
public class AuthSimplifiedController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailCodeService emailCodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/verify-email/send")
    public ResponseEntity<?> sendEmailCodeSimplified(@Valid @RequestBody AuthSimplifiedEmail body) {
        String email = body.getEmail();
        User user = authService.getSimplifiedUser(email);
        emailCodeService.sendUserEmailWithoutVerification(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> loginSimplified(@Valid @RequestBody AuthSimplifiedLogin authSimplifiedLogin) {
        User user = authService.getSimplifiedUser(authSimplifiedLogin.getEmail());

        if (!emailCodeService.validateUserEmailCode(user, authSimplifiedLogin.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        user.setVerifiedEmail(true);

        if (Optional.ofNullable(user.getId()).isEmpty()) {
            userRepository.save(user);
        }

        final String jwt = jwtHelper.generateUserToken(user);
        final String refreshToken = jwtHelper.generateRefreshToken(user);

        RefreshToken newRefreshTokenEntity = RefreshToken
                .builder()
                .token(refreshToken)
                .userId(user.getId())
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        ResponseCookie cookieRefreshToken = cookieService.createRefreshTokenCookie(refreshToken);
        ResponseCookie cookieAccessToken = cookieService.createAccessTokenCookie(jwt);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .body(user);
    }

}
