/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.auth;

import com.yil.account.auth.dto.JwtLoginRequest;
import com.yil.account.auth.dto.JwtRefreshRequest;
import com.yil.account.auth.dto.JwtResponce;
import com.yil.account.base.MD5Util;
import com.yil.account.exception.*;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account/v1/auth")
public class JwtController {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponce> login(@Valid @RequestBody JwtLoginRequest jwtLoginRequest) throws NoSuchAlgorithmException, DisabledUserException, UserNotFoundException, LockedUserException, WrongPasswordException {
        User user = userService.findByUserName(jwtLoginRequest.getUserName());
        if (!user.isEnabled())
            throw new DisabledUserException();
        if (user.isLocked())
            throw new LockedUserException();
        String myHash = MD5Util.encode(jwtLoginRequest.getPassword());
        if (!user.getPassword().equals(myHash))
            throw new WrongPasswordException();
        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.refreshToken(token);
        JwtResponce responce = new JwtResponce();
        responce.setToken(token);
        responce.setRefreshToken(refreshToken);
        return ResponseEntity.ok(responce);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public ResponseEntity<JwtResponce> refresh(@Valid @RequestBody JwtRefreshRequest jwtRefreshRequest) throws JwtExpiredException, UserNotFoundException {
        if (!jwtTokenUtil.validateToken(jwtRefreshRequest.getRefreshToken()))
            throw new JwtExpiredException();
        Long userId = jwtTokenUtil.getUserId(jwtRefreshRequest.getRefreshToken());
        User user = userService.findById(userId);
        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.refreshToken(token);
        JwtResponce responce = new JwtResponce();
        responce.setToken(token);
        responce.setRefreshToken(refreshToken);
        return ResponseEntity.ok(responce);
    }
}