package com.raizes.backend.api.controller;

import com.raizes.backend.api.dto.CriarPedidoRequest;
import com.raizes.backend.api.dto.PedidoResponse;
import com.raizes.backend.application.service.PedidoService;
import com.raizes.backend.domain.model.Pedido.CanalPedido;
import com.raizes.backend.domain.model.Pedido.StatusPedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;



@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CriarPedidoRequest request) {

        PedidoResponse response = pedidoService.criarPedido(
                userDetails.getUsername(), request
        );
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar(
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(required = false) StatusPedido status) {

        return ResponseEntity.ok(
                pedidoService.listarPedidos(canalPedido, status)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {

        return ResponseEntity.ok(
                pedidoService.atualizarStatus(id, status)
        );
    }

}
