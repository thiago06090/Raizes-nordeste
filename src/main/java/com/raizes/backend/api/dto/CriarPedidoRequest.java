package com.raizes.backend.api.dto;

import com.raizes.backend.domain.model.Pedido.CanalPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CriarPedidoRequest {
    @NotNull(message = "Unidade obrigatória")
    private Long unidadeId;

    @NotNull(message = "Canal do pedido obrigatório")
    private CanalPedido canalPedido;

    @NotNull(message = "Forma de pagamento obrigatória")
    private String formaPagamento;

    @NotEmpty(message = "Pedido deve ter ao menos um item")
    private List<ItemPedidoRequest> itens;

    @Data
    public static class ItemPedidoRequest {

        @NotNull(message = "Produto obrigatório")
        private Long produtoId;

        @NotNull(message = "Quantidade obrigatória")
        private Integer quantidade;
    }


}
