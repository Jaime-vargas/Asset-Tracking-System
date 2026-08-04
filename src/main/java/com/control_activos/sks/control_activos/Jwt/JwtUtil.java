package com.control_activos.sks.control_activos.Jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final Long TIME_EXPIRATION;

    public JwtUtil() {
        this.SECRET_KEY = Keys.hmacShaKeyFor(
                "0cRJNYRU8UxYn5eZ0D6SPwHmRHsnpiIB".getBytes(StandardCharsets.UTF_8)
        );
        this.TIME_EXPIRATION = TimeUnit.MINUTES.toMillis(120);
    }

    // Generate token for user authentication
    public String generateToken(String username, String fullName, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim("fullName", fullName)
                .claim("role", role)
                .issuedAt(new java.util.Date(now))
                .expiration(new java.util.Date(now + TIME_EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Generate token with the device id and type of device as claims
    public String generateHardwareToken (Long id){
        // Test Calims
        Map<String, Object> claims = new HashMap<>();
        claims.put("deviceId", id);
        claims.put("type", "camera");

        return Jwts.builder()
                .claims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    // validate the token and return the device id if the token is valid, otherwise throw an exception
    // the token is valid if the signature is valid and the token is not expired
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Get subject from the token
    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Get claim from the token
    public String getClaim(String token, String claimKey) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get(claimKey, String.class);
    }

}
