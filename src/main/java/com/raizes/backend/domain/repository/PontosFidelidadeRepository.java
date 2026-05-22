package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.PontosFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PontosFidelidadeRepository
        extends JpaRepository<PontosFidelidade, Long> {

    Optional<PontosFidelidade> findByUsuarioId(Long usuarioId);
}
