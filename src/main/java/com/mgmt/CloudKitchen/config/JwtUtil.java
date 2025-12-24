package com.mgmt.CloudKitchen.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private String secret = "MySecretKey123456789!@#$%6&*(MySecretKey123456789!@#$%6&*(MySecretKey123456789!@#$%6&*(";


    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authorites = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", authorites)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(getSigningKey())
                .compact();
    }


    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }



    public boolean validateToken(String token, UserDetails userDetails)
    {

        Claims claims = extractClaims(token);
        String username = userDetails.getUsername();
        String tokenUserName = claims.getSubject();
        Date expiration = claims.getExpiration();

        boolean res1 = username.equals(tokenUserName);
        boolean res2 = expiration.before(new Date());

        return (res1 && !res2);
    }


}
