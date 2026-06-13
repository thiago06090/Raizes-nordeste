package com.raizes.backend.application.service;

import com.raizes.backend.api.dto.AuthResponse;
import com.raizes.backend.api.dto.LoginRequest;
import com.raizes.backend.api.dto.RegisterRequest;
import com.raizes.backend.domain.model.PontosFidelidade;
import com.raizes.backend.domain.model.Usuario;
import com.raizes.backend.domain.model.Usuario.Perfil;
import com.raizes.backend.domain.repository.PontosFidelidadeRepository;
import com.raizes.backend.domain.repository.UsuarioRepository;
import com.raizes.backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PontosFidelidadeRepository pontosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditoriaService auditoriaService;

    public AuthResponse registrar(RegisterRequest request) {

        // verifica se e-mail já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        // verifica consentimento LGPD
        if (!request.getConsentimentoLgpd()) {
            throw new RuntimeException(
                    "É necessário aceitar os termos de uso e política de privacidade"
            );
        }

        // cria o usuário
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setPerfil(Perfil.CLIENTE);
        usuario.setConsentimentoLgpd(request.getConsentimentoLgpd());

        // se perfil não informado, padrão é CLIENTE
        if (request.getPerfil() != null) {
            usuario.setPerfil(request.getPerfil());
        } else {
            usuario.setPerfil(Perfil.CLIENTE);
        }

        usuarioRepository.save(usuario);

        auditoriaService.registrar(
                "USUARIO_CADASTRADO",
                usuario.getEmail(),
                "Novo usuário cadastrado no sistema",
                "perfil=" + usuario.getPerfil().name()
        );


        // cria pontos de fidelidade zerados
        PontosFidelidade pontos = new PontosFidelidade();
        pontos.setUsuario(usuario);
        pontosRepository.save(pontos);

        // gera e retorna o token
        String token = jwtService.gerarToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getNome(),
                usuario.getPerfil().name());
    }

    public AuthResponse login(LoginRequest request) {

        // autentica com Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        auditoriaService.registrar(
                "LOGIN",
                request.getEmail(),
                "Usuário realizou login no sistema",
                null
        );

        // busca o usuário no banco
        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado"
                ));

        // gera e retorna o token
        String token = jwtService.gerarToken(usuario.getEmail());
        return new AuthResponse(token, usuario.getNome(),
                usuario.getPerfil().name());
    }

}
