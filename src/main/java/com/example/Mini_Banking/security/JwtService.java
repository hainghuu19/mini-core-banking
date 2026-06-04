package com.example.Mini_Banking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JwtService {

    @Value("${SECRET_KEY}")
   private String jwtSecret;

    @Value("${EXPIRATION_TIME}")
   private int expirationTime;

    private Key getSignKey(){
        byte[] key = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromJwtToken(Claims claims){
        return claims.getSubject();
    }

    public boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());

    }

    public boolean isJwtTokenValid(Claims claims, UserDetails userDetails){
        String username = userDetails.getUsername();
        String usernameFromToken = claims.getSubject();

        return username.equals(usernameFromToken) && !isTokenExpired(claims);
    }

}
