/*
 * Author: GitHub Copilot
 * Created: 19 November 2025
 * Description: JWT utility class for token generation and validation
 */
package com.c11.umastagram.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    @Value("${jwt.expirationMs:86400000}") // Default 24 hours
    private long EXPIRATION_MS;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Generate JWT token for a user
     * @param userId User ID
     * @param username Username
     * @return JWT token string
     */
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("username", username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    /**
     * Validate and parse JWT token
     * @param token JWT token
     * @return Claims if valid
     * @throws io.jsonwebtoken.JwtException if invalid
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    /**
     * Extract user ID from token
     * @param token JWT token
     * @return User ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * Extract username from token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * Check if token is expired
     * @param token JWT token
     * @return true if expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = validateToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
