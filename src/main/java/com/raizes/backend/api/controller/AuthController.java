package com.raizes.backend.api.controller;

import com.raizes.backend.api.dto.AuthResponse;
import com.raizes.backend.api.dto.LoginRequest;
import com.raizes.backend.api.dto.RegisterRequest;
import com.raizes.backend.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrar(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201)
                .body(authService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}