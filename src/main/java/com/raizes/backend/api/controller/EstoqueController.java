package com.raizes.backend.api.controller;

import com.raizes.backend.application.service.EstoqueService;
import com.raizes.backend.domain.model.Estoque;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor

public class EstoqueController {
    private final EstoqueService estoqueService;

    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<Estoque>> listarPorUnidade(
            @PathVariable Long unidadeId) {
        return ResponseEntity.ok(
                estoqueService.listarPorUnidade(unidadeId)
        );
    }

    @PostMapping("/unidade/{unidadeId}/produto/{produtoId}/entrada")
    public ResponseEntity<Estoque> entrada(
            @PathVariable Long unidadeId,
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(
                estoqueService.entrada(unidadeId, produtoId, quantidade)
        );
    }

    @PostMapping("/unidade/{unidadeId}/produto/{produtoId}/saida")
    public ResponseEntity<Estoque> saida(
            @PathVariable Long unidadeId,
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(
                estoqueService.saida(unidadeId, produtoId, quantidade)
        );
    }
}
