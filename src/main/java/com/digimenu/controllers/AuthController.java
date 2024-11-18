package com.digimenu.controllers;

import com.digimenu.dto.TokenDTO;
import com.digimenu.dto.auth.AuthLogin;
import com.digimenu.dto.auth.AuthLoginCompany;
import com.digimenu.dto.auth.AuthRegister;
import com.digimenu.exception.InternalErrorException;
import com.digimenu.models.RefreshToken;
import com.digimenu.models.User;
import com.digimenu.repository.RefreshTokenRepository;
import com.digimenu.repository.UserRepository;
import com.digimenu.security.JwtHelper;
import com.digimenu.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

        RefreshToken refreshTokenEntity = new RefreshToken(refreshToken, user);
        refreshTokenRepository.save(refreshTokenEntity);

        TokenDTO tokenDTO = new TokenDTO(user.getId(), jwt, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);
    }

    @PostMapping("/login/anonymous")
    public ResponseEntity<?> loginAnonymous() {
        String jwt = jwtHelper.generateAnonymousToken();
        TokenDTO tokenDTO = new TokenDTO(null, jwt, null);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        User user = authService.registerUser(authRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO body) {
        String refreshToken = body.getRefreshToken();

        RefreshToken refreshTokenEntity = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new InternalErrorException("Refresh token not found"));

        refreshTokenRepository.delete(refreshTokenEntity);

        String email = jwtHelper.extractRefreshTokenEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InternalErrorException("User not found"));

        String jwt = jwtHelper.generateUserToken(user);
        String newRefreshToken = jwtHelper.generateRefreshToken(user);

        RefreshToken newRefreshTokenEntity = new RefreshToken(newRefreshToken, user);
        refreshTokenRepository.save(newRefreshTokenEntity);

        TokenDTO tokenDTO = new TokenDTO(user.getId(), jwt, newRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);
    }
}
