/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.exception;

import com.yil.account.base.ApiException;
import com.yil.account.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@ApiException(code = ErrorCode.DisabledUser)
public class DisabledUserException extends Exception {
}
