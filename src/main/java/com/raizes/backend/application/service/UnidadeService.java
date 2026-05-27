package com.raizes.backend.application.service;

import com.raizes.backend.domain.model.Unidade;
import com.raizes.backend.domain.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeService {
    private final UnidadeRepository unidadeRepository;

    public List<Unidade> listarTodas() {
        return unidadeRepository.findAll();
    }

    public List<Unidade> listarAtivas() {
        return unidadeRepository.findByAtivaTrue();
    }

    public Unidade buscarPorId(Long id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Unidade não encontrada"
                ));
    }

    public Unidade criar(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

    public Unidade atualizar(Long id, Unidade unidadeAtualizada) {
        Unidade unidade = buscarPorId(id);
        unidade.setNome(unidadeAtualizada.getNome());
        unidade.setCidade(unidadeAtualizada.getCidade());
        unidade.setEstado(unidadeAtualizada.getEstado());
        unidade.setAtiva(unidadeAtualizada.getAtiva());
        return unidadeRepository.save(unidade);
    }
}
