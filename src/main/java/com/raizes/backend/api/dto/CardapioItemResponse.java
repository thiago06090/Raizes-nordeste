package com.raizes.backend.api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CardapioItemResponse {

    private Long produtoId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeDisponivel;
    private Boolean disponivel;
}