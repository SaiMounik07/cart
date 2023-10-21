package com.mini.ecommerce.cart.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${spring.security.key}")
    private String SECURITY_KEY;
    @Value("${long.token.expirationTime}")
    private long expirationTime;
    public String extractEmail(String jwt){
       return extractClaim(jwt,Claims::getSubject);
    }
    public String generateJwtToken(UserDetails userDetails){
        return generateJwtToken(new HashMap<>(),userDetails);
    }
    public Date extractExpirationTime(String jwt){
        return extractClaim(jwt,Claims::getExpiration);
    }
    public boolean isTokenValid(String jwt,UserDetails userDetails){
        String userName=extractEmail(jwt);
        return (userName.equals(userDetails.getUsername())&& !isTokenExpired(jwt));
    }

    public boolean isTokenExpired(String jwt) {
        return extractExpirationTime(jwt).before(new Date());
    }
    public String generateJwtToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .signWith(getSigningKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*expirationTime))
                .compact();
    }

    public <T> T extractClaim(String jwt, Function<Claims,T> specificClaim){
        final Claims claims=extractAllClaims(jwt);
        return specificClaim.apply(claims);
    }
    public Claims extractAllClaims(String jwt){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keys = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        return Keys.hmacShaKeyFor(keys);
    }
}
