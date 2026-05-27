package com.raizes.backend.api.controller;


import com.raizes.backend.application.service.FidelidadeService;
import com.raizes.backend.domain.model.PontosFidelidade;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/fidelidade")
@RequiredArgsConstructor



public class FidelidadeController {
    private final FidelidadeService fidelidadeService;
    private String senha;

    @GetMapping("/saldo")
    public ResponseEntity<PontosFidelidade> consultarSaldo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                fidelidadeService.consultarSaldo(
                        userDetails.getUsername()
                )
        );
    }

    @PostMapping("/resgatar")
    public ResponseEntity<PontosFidelidade> resgatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer pontos) {
        return ResponseEntity.ok(
                fidelidadeService.resgatar(
                        userDetails.getUsername(), pontos
                )
        );
    }
}
