package com.raizes.backend.api.dto;

import com.raizes.backend.domain.model.Pedido.CanalPedido;
import com.raizes.backend.domain.model.Pedido.StatusPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {
    private Long id;
    private String nomeCliente;
    private String nomeUnidade;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<ItemResponse> itens;
    private String statusPagamento;

    @Data
    public static class ItemResponse {
        private Long produtoId;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;
    }
}
