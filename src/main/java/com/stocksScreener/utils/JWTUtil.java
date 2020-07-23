package com.stocksScreener.utils;

import com.stocksScreener.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtil {

    private final String SECRET_KEY;

    public JWTUtil(@Value("${jwt.secretKey}") String secret_key) {
        this.SECRET_KEY = secret_key;
    }


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserEmail());

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder().setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .compact();
    }

    public Claims extractAllClaims(String token) throws SignatureException {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiry(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiry(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            final String username = extractUsername(token);
            return username != null && !username.equals("") && !isTokenExpired(token);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
