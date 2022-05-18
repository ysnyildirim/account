package com.yil.authentication.auth.controller;

import com.yil.authentication.auth.dto.JwtRequest;
import com.yil.authentication.auth.dto.JwtResponse;
import com.yil.authentication.auth.util.JwtTokenUtil;
import com.yil.authentication.base.MD5Util;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/v1/auth")
public class JwtAuthenticationController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    @Autowired
    public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        try {
            Objects.requireNonNull(jwtRequest.getUsername());
            User user = userService.findByUserNameAndDeletedTimeIsNull(jwtRequest.getUsername());
            String myHash = MD5Util.encode(jwtRequest.getPassword());
            if (!user.getPassword().equals(myHash))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception exception) {
            logger.error("Hata oluştu!", exception);

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
