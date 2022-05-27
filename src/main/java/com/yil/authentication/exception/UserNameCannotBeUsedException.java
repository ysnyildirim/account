/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.authentication.exception;

import com.yil.authentication.error.ApiException;
import com.yil.authentication.error.ErrorCode;

@ApiException(code = ErrorCode.UserNameCannotBeUsed)
public class UserNameCannotBeUsedException extends RuntimeException {

}
