/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.auth;

import com.yil.account.user.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        final Date createdDate = new Date(System.currentTimeMillis());
        final Date expirationDate = calculateExpirationDate(createdDate);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userName", user.getUserName());
        claims.put("mail", user.getMail());
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUserName())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    public Long getUserId(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return (Long) getClaimFromToken(token, f -> f.get("userId"));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        String authToken = token;
        if (token != null && token.startsWith("Bearer ")) {
            authToken = token.substring(7);
        }
        final Claims claims = getAllClaimsFromToken(authToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getUserNameFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAtDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        if (isTokenExpired(token))
            return false;
        final String userName = getUserNameFromToken(token);
        return true;
    }

    public String refreshToken(String token) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.MINUTE, 5 * 60);
        Date createdDate = c.getTime();
        Date expirationDate = calculateExpirationDate(createdDate);
        Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);
        claims.setId(UUID.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}