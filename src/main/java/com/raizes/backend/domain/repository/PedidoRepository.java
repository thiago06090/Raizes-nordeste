package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.Pedido;
import com.raizes.backend.domain.model.Pedido.CanalPedido;
import com.raizes.backend.domain.model.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository
        extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByCanalPedido(CanalPedido canalPedido);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByCanalPedidoAndStatus(
            CanalPedido canalPedido, StatusPedido status
    );

    List<Pedido> findByUnidadeId(Long unidadeId);
}
