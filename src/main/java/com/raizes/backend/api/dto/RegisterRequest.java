package com.raizes.backend.api.dto;

import com.raizes.backend.domain.model.Usuario.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Nome obrigatório")
    private String nome;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail obrigatório")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    private String senha;

    @NotNull(message = "Consentimento LGPD obrigatório")
    private Boolean consentimentoLgpd;

    // Se não informado, padrão é CLIENTE
    private Perfil perfil;

}
