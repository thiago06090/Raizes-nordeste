package com.raizes.backend.api.controller;

import com.raizes.backend.application.service.UnidadeService;
import com.raizes.backend.domain.model.Unidade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor

public class UnidadeController {
    private final UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<List<Unidade>> listar() {
        return ResponseEntity.ok(unidadeService.listarAtivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Unidade> criar(
            @RequestBody Unidade unidade) {
        return ResponseEntity.status(201)
                .body(unidadeService.criar(unidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unidade> atualizar(
            @PathVariable Long id,
            @RequestBody Unidade unidade) {
        return ResponseEntity.ok(
                unidadeService.atualizar(id, unidade)
        );
    }
}
