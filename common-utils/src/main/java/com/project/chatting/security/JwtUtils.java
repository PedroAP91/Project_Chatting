package com.project.chatting.security;

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
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Generar Access Token con nick (si lo necesitas)
    public String generateAccessToken(String email, String nick) {
        return Jwts.builder()
                .setSubject(email)
                .claim("nick", nick)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Generar Refresh Token (válido por 7 días) con claim "type": "refresh"
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar Token
    public boolean validateToken(String token, boolean isRefresh) {
        try {
            SecretKey secretKey = isRefresh ? refreshKey : key;
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            if (isRefresh) {
                Object type = jwsClaims.getBody().get("type");
                System.out.println("Refresh token type: " + type);
                if (type == null || !type.equals("refresh")) {
                    System.err.println("⚠ Refresh token inválido: falta o es incorrecto el claim 'type'");
                    return false;
                }
            }
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
    public Object getClaimFromToken(String token, String claimName, boolean isRefresh) {
        SecretKey secretKey = isRefresh ? refreshKey : key;
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claimName);
    }

    // Sobrecarga para llamadas sin el parámetro booleano (asume false)
    public String getSubjectFromToken(String token) {
        return getSubjectFromToken(token, false);
    }
}


