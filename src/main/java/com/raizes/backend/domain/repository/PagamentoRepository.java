package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagamentoRepository
        extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedidoId(Long pedidoId);
}
