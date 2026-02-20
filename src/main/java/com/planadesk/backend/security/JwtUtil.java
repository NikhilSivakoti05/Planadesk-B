//package com.planadesk.backend.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    private Key key(){
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public String generateToken(String email,String role){
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("role",role)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+expiration))
//                .signWith(key(),SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String getEmail(String token){
//        return parse(token).getSubject();
//    }
//
//    public String getRole(String token){
//        return parse(token).get("role",String.class);
//    }
//
//    public boolean valid(String token){
//        try{ parse(token); return true; }
//        catch(Exception e){ return false; }
//    }
//
//    private Claims parse(String token){
//        return Jwts.parserBuilder().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody();
//    }
//}

package com.planadesk.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String ISSUER = "planadesk-api";

    private final Key signingKey;
    private final long expirationMillis;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {

        // 🔥 Enforce strong secret (>= 256 bits)
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException(
                "JWT secret must be at least 32 characters (256 bits)");
        }

        this.signingKey =
            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        this.expirationMillis = expirationMillis;
    }

    // ---------------- TOKEN GENERATION ----------------
    public String generateToken(String email, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------- VALIDATION ----------------
    public boolean valid(String token) {
        try {
            parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false; // token expired
        } catch (JwtException | IllegalArgumentException e) {
            return false; // malformed / invalid
        }
    }

    // ---------------- CLAIMS ----------------
    public String getEmail(String token) {
        return parse(token).getSubject();
    }

    public String getRole(String token) {
        return parse(token).get("role", String.class);
    }

    // ---------------- INTERNAL PARSER ----------------
    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(ISSUER)
                .setAllowedClockSkewSeconds(60) // ⏱ clock skew tolerance
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

