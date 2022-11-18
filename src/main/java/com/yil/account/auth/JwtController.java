/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.auth;

import com.yil.account.auth.dto.JwtLoginRequest;
import com.yil.account.auth.dto.JwtRefreshRequest;
import com.yil.account.auth.dto.JwtResponce;
import com.yil.account.auth.service.JwtTokenUtil;
import com.yil.account.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/acc/v1/auth")
public class JwtController {
    private final JwtTokenUtil jwtTokenUtil;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponce> login(@Valid @RequestBody JwtLoginRequest jwtLoginRequest) throws NoSuchAlgorithmException, UserNotFoundException, LockedUserException, WrongPasswordException {
        JwtResponce responce = jwtTokenUtil.login(jwtLoginRequest);
        return ResponseEntity.ok(responce);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public ResponseEntity<JwtResponce> refresh(@Valid @RequestBody JwtRefreshRequest jwtRefreshRequest) throws RefreshTokenNotFoundException, RefreshTokenExpiredException, LockedUserException, UserNotFoundException {
        JwtResponce jwtResponce = jwtTokenUtil.refresh(jwtRefreshRequest);
        return ResponseEntity.ok(jwtResponce);
    }
}