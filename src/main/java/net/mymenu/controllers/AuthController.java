package net.mymenu.controllers;

import net.mymenu.dto.auth.AuthVerifyEmail;
import net.mymenu.dto.auth.AuthLogin;
import net.mymenu.dto.auth.AuthRegister;
import net.mymenu.exception.AccountAlreadyVerifiedException;
import net.mymenu.exception.EmailCodeRequestTooSoonException;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.RefreshToken;
import net.mymenu.models.User;
import net.mymenu.models.auth.EmailCode;
import net.mymenu.repository.RefreshTokenRepository;
import net.mymenu.repository.UserRepository;
import net.mymenu.repository.auth.EmailCodeRepository;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.AuthService;
import jakarta.validation.Valid;
import net.mymenu.service.CookieService;
import net.mymenu.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EmailCodeRepository emailCodeRepository;

    @Autowired
    private CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLogin authLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authLogin.getEmail(), authLogin.getPassword())
        );

        final User user = userRepository.findByEmail(authLogin.getEmail())
                .orElseThrow(() -> new SecurityException("User authenticated but not found"));

        final String jwt = jwtHelper.generateUserToken(user);
        final String refreshToken = jwtHelper.generateRefreshToken(user);

        return authService.getResponseEntity(user, jwt, refreshToken);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody AuthVerifyEmail authVerifyEmail) {
        User user = jwtHelper.extractUser();

        if (!authService.validateEmailCode(user, authVerifyEmail.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.setVerifiedEmail(true);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/verify-email/send")
    public ResponseEntity<?> sendEmailCode() {
        User user = jwtHelper.extractUser();

        if (user.isVerifiedEmail()) {
            throw new AccountAlreadyVerifiedException("Your account is already verified");
        }

        List<EmailCode> lastUserEmailCode = emailCodeRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .orElse(null);

        LocalDateTime now = LocalDateTime.now();

        if (lastUserEmailCode != null
                && !lastUserEmailCode.isEmpty()
                && lastUserEmailCode.getFirst().getCreatedAt().plusMinutes(1).isAfter(now)) {
            throw new EmailCodeRequestTooSoonException("Wait until 1 minute to send other code");
        }

        EmailCode emailCode = authService.createEmailCode(user);

        Context context = new Context();
        context.setVariable("nome", user.getName().split(" ")[0]);
        context.setVariable("code", emailCode.getCode());

        emailSenderService.sendEmail(
                user.getEmail(),
                "MyMenu - Verifique sua conta",
                "email-verification",
                context);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login/anonymous")
    @Deprecated
    public ResponseEntity<?> loginAnonymous() {
        String jwt = jwtHelper.generateAnonymousToken();
        ResponseCookie cookieAccessToken = cookieService.createAccessTokenCookie(jwt);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieAccessToken.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        User user = authService.registerUser(authRegister);

        final String jwt = jwtHelper.generateUserToken(user);
        final String refreshToken = jwtHelper.generateRefreshToken(user);

        return authService.getResponseEntity(user, jwt, refreshToken, HttpStatus.CREATED);
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

            String jwt = jwtHelper.generateUserToken(user);
            String newRefreshToken = jwtHelper.generateRefreshToken(user);

            return authService.getResponseEntity(user, jwt, newRefreshToken);
        } catch (AccountStatusException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", defaultValue = "null") String refreshToken) {
        if (refreshToken.equals("null")) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        refreshTokenRepository.findById(refreshToken)
                .ifPresent(refreshTokenEntity -> refreshTokenRepository.delete(refreshTokenEntity));

        ResponseCookie cookieRefreshToken = cookieService.createCookie("refreshToken", "", 0, "/auth");
        ResponseCookie cookieAccessToken = cookieService.createCookie("accessToken", "", 0, "/");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .build();
    }
}
