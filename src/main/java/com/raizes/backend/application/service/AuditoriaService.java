package com.raizes.backend.application.service;

import com.raizes.backend.domain.model.Auditoria;
import com.raizes.backend.domain.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public void registrar(String acao, String usuarioEmail,
                          String descricao, String dadosExtras) {

        // salva no banco
        Auditoria auditoria = new Auditoria();
        auditoria.setAcao(acao);
        auditoria.setUsuarioEmail(usuarioEmail);
        auditoria.setDescricao(descricao);
        auditoria.setDadosExtras(dadosExtras);
        auditoriaRepository.save(auditoria);

        // registra no console
        log.info("[AUDITORIA] acao={} | usuario={} | {} | {}",
                acao, usuarioEmail, descricao, dadosExtras);
    }

    public List<Auditoria> listarTodos() {
        return auditoriaRepository
                .findAll();
    }

    public List<Auditoria> listarPorUsuario(String email) {
        return auditoriaRepository
                .findByUsuarioEmailOrderByCreatedAtDesc(email);
    }

    public List<Auditoria> listarPorAcao(String acao) {
        return auditoriaRepository
                .findByAcaoOrderByCreatedAtDesc(acao);
    }
}