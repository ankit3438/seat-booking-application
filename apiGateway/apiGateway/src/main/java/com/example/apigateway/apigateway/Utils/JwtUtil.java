package com.example.apigateway.apigateway.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // 🔥 Must be at least 32 characters
    private final String SECRET = "MyVerySecureSecretKeyForJWTAuthenticationThatIsAtLeast256BitsLongForHS256Algorithm2025";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token); // validates signature + expiry
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}