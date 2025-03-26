package com.uni.bankproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_DATE;

    public String getToken(UserDetails user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            return getToken(claims, user);
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                    .signWith(getKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token with extra claims", e);
        }
    }

    private Key getKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving username from token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            throw new RuntimeException("Error validating token", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            throw new RuntimeException("Error validating token", e);
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all claims from token", e);
        }
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving claim from token", e);
        }
    }

    private Date getExpirationDateFromToken(String token) {
        try {
            return getClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving expiration date from token", e);
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return getExpirationDateFromToken(token).before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if token is expired", e);
        }
    }
}