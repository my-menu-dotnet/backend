package com.digimenu.controllers;

import com.digimenu.dto.auth.AuthLogin;
import com.digimenu.dto.auth.AuthLoginCompany;
import com.digimenu.dto.auth.AuthRegister;
import com.digimenu.exception.InternalErrorException;
import com.digimenu.models.User;
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLogin authLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authLogin.getEmail(), authLogin.getPassword())
        );

        final User user = userRepository.findByEmail(authLogin.getEmail())
                .orElseThrow(() -> new InternalErrorException("User authenticated but not found"));

        final String jwt = jwtHelper.generateTemporaryToken(user);

        return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + jwt).build();
    }

    @PostMapping("/login/company")
    public ResponseEntity<?> loginCompany(@Valid @RequestBody AuthLoginCompany authLoginCompany) {
        User user = jwtHelper.extractUser();
        UUID companyId = authLoginCompany.getCompanyId();

        authService.validateUserHasCompany(user, companyId);

        String jwt = jwtHelper.generateToken(user, companyId);
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + jwt).build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody AuthRegister authRegister) {
        User user = authService.registerUser(authRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
