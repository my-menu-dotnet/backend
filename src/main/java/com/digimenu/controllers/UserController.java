package com.digimenu.controllers;

import com.digimenu.models.User;
import com.digimenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    public ResponseEntity<User> getUserByToken() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
