package com.raizes.backend.api.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String nome;
    private String perfil;

    public AuthResponse(String token, String nome, String perfil) {
        this.accessToken = token;
        this.nome = nome;
        this.perfil = perfil;
    }
}
