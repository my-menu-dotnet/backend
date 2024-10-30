package com.digimenu.controllers;

import com.digimenu.service.FoodService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathParam("id") UUID id) {
        return ResponseEntity.ok(foodService.findById(id));
    }
}
