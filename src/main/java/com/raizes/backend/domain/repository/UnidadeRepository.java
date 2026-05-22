package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnidadeRepository
        extends JpaRepository<Unidade, Long> {

    List<Unidade> findByAtivaTrue();
}
