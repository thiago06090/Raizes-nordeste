package com.raizes.backend.application.service;

import com.raizes.backend.domain.model.PontosFidelidade;
import com.raizes.backend.domain.model.Usuario;
import com.raizes.backend.domain.repository.PontosFidelidadeRepository;
import com.raizes.backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FidelidadeService {
    private final PontosFidelidadeRepository pontosRepository;
    private final UsuarioRepository usuarioRepository;

    public PontosFidelidade consultarSaldo(String emailCliente) {
        Usuario usuario = usuarioRepository
                .findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado"
                ));

        return pontosRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Pontos não encontrados"
                ));
    }

    public PontosFidelidade resgatar(
            String emailCliente, Integer pontos) {

        PontosFidelidade fidelidade = consultarSaldo(emailCliente);

        if (fidelidade.getSaldo() < pontos) {
            throw new RuntimeException(
                    "Saldo insuficiente. Disponível: "
                            + fidelidade.getSaldo() + " pontos"
            );
        }

        fidelidade.setSaldo(fidelidade.getSaldo() - pontos);
        return pontosRepository.save(fidelidade);
    }
}
