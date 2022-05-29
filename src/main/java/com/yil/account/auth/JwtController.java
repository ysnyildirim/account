/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.auth;

import com.yil.account.base.MD5Util;
import com.yil.account.exception.DisabledUserException;
import com.yil.account.exception.LockedUserException;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.exception.WrongPasswordException;
import com.yil.account.user.dto.JwtResponce;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/account/v1/auth")
public class JwtController {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    @Autowired
    public JwtController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/login")
    public ResponseEntity<JwtResponce> login(@Valid @RequestParam @NotBlank @Length(min = 1, max = 100) String userName,
                                             @Valid @RequestParam @NotBlank @Length(min = 1, max = 100) String password) throws NoSuchAlgorithmException, DisabledUserException, UserNotFoundException, LockedUserException, WrongPasswordException {
        User user = userService.getActiveUser(userName);
        String myHash = MD5Util.encode(password);
        if (!user.getPassword().equals(myHash))
            throw new WrongPasswordException();
        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.refreshToken(token);
        JwtResponce responce = new JwtResponce();
        responce.setToken(token);
        responce.setRefreshToken(refreshToken);
        return ResponseEntity.ok(responce);
    }


}