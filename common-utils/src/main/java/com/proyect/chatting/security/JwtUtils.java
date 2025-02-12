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
    private final String secret;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            this.key = Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Error al decodificar jwt.secret, revisa que sea una clave Base64 válida.", e);
        }
    }


    @PostConstruct
    public void init() {
        System.out.println("🔑 Clave secreta JWT cargada correctamente: " + (secret != null ? "OK" : "NO CARGADA"));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(key, SignatureAlgorithm.HS256) // Explicitamos el algoritmo
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("⚠ Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("⚠ Token no soportado: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("⚠ Token mal formado: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("⚠ Firma inválida: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("⚠ Argumento inválido: " + e.getMessage());
        }
        return false;
    }

    public String getSubjectFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            System.err.println("⚠ Error obteniendo subject del token: " + e.getMessage());
            return null;
        }
    }
}
