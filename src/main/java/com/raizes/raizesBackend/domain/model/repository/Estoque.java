package com.raizes.raizesBackend.domain.model.repository;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estoque")

public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade = 0;

}
