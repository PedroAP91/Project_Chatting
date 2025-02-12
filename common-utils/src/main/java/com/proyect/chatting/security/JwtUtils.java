package com.proyect.chatting.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final SecretKey refreshKey;
    private final String secret;
    private final String refreshSecret;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.refresh-secret}") String refreshSecret) {
        this.secret = secret;
        this.refreshSecret = refreshSecret;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.refreshKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(refreshSecret));
    }

    @PostConstruct
    public void init() {
        System.out.println("🔑 Clave secreta JWT cargada correctamente: " + (secret != null ? "OK" : "NO CARGADA"));
    }

    // Generar Token de Acceso (válido por 1 hora)
    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Generar Refresh Token (válido por 7 días)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 días
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar Token
    public boolean validateToken(String token, boolean isRefresh) {
        try {
            SecretKey secretKey = isRefresh ? refreshKey : key;
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("⚠ Token expirado: " + e.getMessage());
        } catch (JwtException e) {
            System.err.println("⚠ Token inválido: " + e.getMessage());
        }
        return false;
    }

    public String getSubjectFromToken(String token, boolean isRefresh) {
        SecretKey secretKey = isRefresh ? refreshKey : key;
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
