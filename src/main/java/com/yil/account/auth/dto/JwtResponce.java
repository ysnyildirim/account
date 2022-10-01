/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponce implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String token;
    private String refreshToken;
    private String type = "Bearer";
}