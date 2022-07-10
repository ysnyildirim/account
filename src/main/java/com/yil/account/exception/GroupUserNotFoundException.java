/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.exception;

import com.yil.workflow.base.ApiException;
import com.yil.workflow.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ApiException(code = ErrorCode.GroupUserNotFound)
public class GroupUserNotFoundException extends Exception {
}
