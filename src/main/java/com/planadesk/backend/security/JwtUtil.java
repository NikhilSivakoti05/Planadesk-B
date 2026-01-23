package com.planadesk.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email,String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key(),SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(String token){
        return parse(token).getSubject();
    }

    public String getRole(String token){
        return parse(token).get("role",String.class);
    }

    public boolean valid(String token){
        try{ parse(token); return true; }
        catch(Exception e){ return false; }
    }

    private Claims parse(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
    }
}
