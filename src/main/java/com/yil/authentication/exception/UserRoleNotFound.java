/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.authentication.exception;

import com.yil.authentication.error.ApiException;
import com.yil.authentication.error.ErrorCode;

import javax.persistence.EntityNotFoundException;

@ApiException(code = ErrorCode.UserRoleNotFound)
public class UserRoleNotFound extends EntityNotFoundException {

}
