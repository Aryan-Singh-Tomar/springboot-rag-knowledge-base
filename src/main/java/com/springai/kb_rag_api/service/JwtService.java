package com.springai.kb_rag_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.accessMinutes}") long accessMinutes,
                      @Value("${app.jwt.refreshDays}") long refreshDays) {

        // HMAC key requires enough length; keep secret 32+ chars
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public String generateAccessToken(Long userId, String username, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes * 60);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("uid", userId)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, String username) {
        String jti = UUID.randomUUID().toString();

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshDays * 24 * 60 * 60);

        return Jwts.builder()
                .setSubject(username)
                .setId(jti)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("uid", userId)
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseAndValidate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Long extractUserId(String token) {
        Claims c = parseAndValidate(token).getBody();
        Object uid = c.get("uid");
        if (uid instanceof Number n) return n.longValue();
        throw new IllegalArgumentException("uid claim missing");
    }

    public String extractRole(String token) {
        Claims c = parseAndValidate(token).getBody();
        Object role = c.get("role");
        return role != null ? role.toString() : null;
    }

    public boolean isRefreshToken(String token) {
        Claims c = parseAndValidate(token).getBody();
        Object t = c.get("type");
        return t != null && "refresh".equals(t.toString());
    }

    public String extractJti(String token) {
        return parseAndValidate(token).getBody().getId();
    }

    public Instant extractExpiry(String token) {
        Date exp = parseAndValidate(token).getBody().getExpiration();
        return exp.toInstant();
    }

    public Long extractUserIdFromRefreshTokenOrThrow(String token) {
        Claims c = parseAndValidate(token).getBody();
        Object t = c.get("type");
        if (t == null || !"refresh".equals(t.toString())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Object uid = c.get("uid");
        if (uid instanceof Number n) return n.longValue();
        throw new IllegalArgumentException("uid claim missing");
    }


    public String extractUsername(String token) {
        return parseAndValidate(token).getBody().getSubject();
    }

}
