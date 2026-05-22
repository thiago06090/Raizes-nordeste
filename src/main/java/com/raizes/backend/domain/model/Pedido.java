package com.raizes.backend.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")


public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_pedido", nullable = false)
    private CanalPedido canalPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public enum CanalPedido {
        APP, TOTEM, BALCAO, PICKUP, WEB
    }

    public enum StatusPedido {
        AGUARDANDO_PAGAMENTO,
        PAGO,
        EM_PREPARO,
        PRONTO,
        ENTREGUE,
        CANCELADO
    }

}
