package com.example.oauth2clientservice.security.jwt;

import com.example.oauth2clientservice.exception.BusinessException;
import com.example.oauth2clientservice.security.model.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    @Value("${jwt.confirmation.secret}")
    private String confirmationTokenSecret;

    @Value("${jwt.confirmation.expiration}")
    private long confirmationTokenExpiration;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getKey(accessTokenSecret))
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey(refreshTokenSecret))
                .compact();
    }

    public String generateConfirmationToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + confirmationTokenExpiration))
                .signWith(getKey(confirmationTokenSecret))
                .compact();
    }

    private Key getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public String extractUsernameFromConfirmationToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(confirmationTokenSecret))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String extractUsernameFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(accessTokenSecret))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String extractUsernameFromRefreshToken(String refreshToken) throws BusinessException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(refreshTokenSecret))
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody().getSubject();
        } catch (Exception ex) {
            throw new BusinessException("Token not found or invalid.");
        }
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey(accessTokenSecret))
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
