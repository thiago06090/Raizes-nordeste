package com.raizes.backend.domain.repository;

import com.raizes.backend.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}