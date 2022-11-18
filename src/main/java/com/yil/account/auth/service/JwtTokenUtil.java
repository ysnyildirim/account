/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.auth.service;

import com.yil.account.auth.dao.RefreshTokenDao;
import com.yil.account.auth.dto.JwtLoginRequest;
import com.yil.account.auth.dto.JwtRefreshRequest;
import com.yil.account.auth.dto.JwtResponce;
import com.yil.account.auth.model.RefreshToken;
import com.yil.account.base.MD5Util;
import com.yil.account.exception.*;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;
    private final RefreshTokenDao refreshTokenDao;
    private final UserService userService;
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshExpirationMs;
    @Value("${jwt.secret}")
    private String secret;

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

    public Date getIssuedAtDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Boolean validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        if (isTokenExpired(token))
            return false;
        return true;
    }

    private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public JwtResponce refresh(JwtRefreshRequest jwtRefreshRequest) throws RefreshTokenNotFoundException, RefreshTokenExpiredException, UserNotFoundException, LockedUserException {
        RefreshToken token = refreshTokenDao.findById(jwtRefreshRequest.getRefreshToken()).orElseThrow(RefreshTokenNotFoundException::new);
        if (token.getExpiryDate().compareTo(new Date()) < 0)
            throw new RefreshTokenExpiredException();
        User user = userService.findById(token.getUserId());
        if (user.isLocked())
            throw new LockedUserException();
        JwtResponce responce = generateToken(user);
        refreshTokenDao.delete(token);
        return responce;
    }

    private JwtResponce generateToken(User user) {
        final Date createdDate = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(createdDate.getTime() + expiration);
        final Date refreshExpirationDate = new Date(createdDate.getTime() + expiration + refreshExpirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("mail", user.getMail());
        String token = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUserName())
                .setIssuedAt(createdDate)
                .setNotBefore(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(user.getId());
        refreshToken.setExpiryDate(refreshExpirationDate);
        refreshTokenDao.save(refreshToken);

        JwtResponce responce = new JwtResponce();
        responce.setToken(token);
        responce.setRefreshToken(refreshToken.getToken());
        return responce;
    }

    public JwtResponce login(JwtLoginRequest jwtLoginRequest) throws UserNotFoundException, LockedUserException, NoSuchAlgorithmException, WrongPasswordException {
        User user = userService.findByUserName(jwtLoginRequest.getUserName());
        if (user.isLocked())
            throw new LockedUserException();
        String myHash = MD5Util.encode(jwtLoginRequest.getPassword());
        if (!user.getPassword().equals(myHash))
            throw new WrongPasswordException();
        return generateToken(user);
    }
}