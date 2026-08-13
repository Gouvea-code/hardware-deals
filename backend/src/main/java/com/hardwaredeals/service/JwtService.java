package com.hardwaredeals.service;

import com.hardwaredeals.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final Duration accessTtl;

    public JwtService(@Value("${app.auth.jwt-secret}") String secret,
                      @Value("${app.auth.access-token-ttl:PT15M}") Duration accessTtl) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("app.auth.jwt-secret must contain at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = accessTtl;
    }

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getId().toString()).claim("email", user.getEmail())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plus(accessTtl)))
                .id(UUID.randomUUID().toString()).signWith(key).compact();
    }

    public String getSubject(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public long expiresInSeconds() { return accessTtl.toSeconds(); }
}
