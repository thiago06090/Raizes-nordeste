package com.raizes.backend.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // gera o token a partir do email do usuário
    public String gerarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getChave())
                .compact();
    }

    // extrai o email do token
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    // verifica se o token é válido
    public boolean tokenValido(String token, String email) {
        String emailDoToken = extrairEmail(token);
        return emailDoToken.equals(email) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
