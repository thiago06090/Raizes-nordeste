package com.raizes.backend.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail obrigatório")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    private String senha;

}
