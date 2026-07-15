package com.rescue.rescue.sercurity.jwt;

import com.rescue.rescue.sercurity.user.RescueUserDetail;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        RescueUserDetail userPrincipal = (RescueUserDetail) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getPhoneNumber())
                .claim("id", userPrincipal.getId())
                .claim("Role", roles)
                .claim("Name", userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + this.expirationTime))
                .signWith(this.key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String Token) {
        return Jwts.parser()
                .setSigningKey(this.key())
                .build()
                .parseClaimsJws(Token)
                .getBody()
                .getSubject();
    }

    public Long getIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
    }   

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException  |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());

        }
    }
}
