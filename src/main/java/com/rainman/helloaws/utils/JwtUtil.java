package com.rainman.helloaws.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key (最好放到配置文件里)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 过期时间（1小时）
    private final long expiration = 60 * 60 * 1000;

    // Generate JWT token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Subject: 用户邮箱
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(key)
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
