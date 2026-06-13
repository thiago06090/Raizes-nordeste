package com.raizes.backend.api.controller;

import com.raizes.backend.api.dto.CardapioItemResponse;
import com.raizes.backend.application.service.CardapioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/cardapio")
@RequiredArgsConstructor
public class CardapioController {

    private final CardapioService cardapioService;

    @Operation(summary = "Consultar cardápio por unidade",
            description = "Retorna todos os produtos " +
                    "disponíveis na unidade com quantidade em estoque")
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<CardapioItemResponse>> buscarPorUnidade(
            @PathVariable Long unidadeId) {

        return ResponseEntity.ok(
                cardapioService.buscarCardapioPorUnidade(unidadeId)
        );
    }
}