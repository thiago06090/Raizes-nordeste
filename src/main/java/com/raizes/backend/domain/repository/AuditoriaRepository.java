package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository
        extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByUsuarioEmailOrderByCreatedAtDesc(
            String email);

    List<Auditoria> findByAcaoOrderByCreatedAtDesc(String acao);
}