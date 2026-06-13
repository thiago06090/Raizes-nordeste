package com.raizes.backend.api.controller;

import com.raizes.backend.application.service.AuditoriaService;
import com.raizes.backend.domain.model.Auditoria;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodos() {
        return ResponseEntity.ok(
                auditoriaService.listarTodos()
        );
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<List<Auditoria>> listarPorUsuario(
            @PathVariable String email) {
        return ResponseEntity.ok(
                auditoriaService.listarPorUsuario(email)
        );
    }

    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<Auditoria>> listarPorAcao(
            @PathVariable String acao) {
        return ResponseEntity.ok(
                auditoriaService.listarPorAcao(acao)
        );
    }
}