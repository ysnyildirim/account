/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.authentication.exception;

import com.yil.authentication.base.ApiException;
import com.yil.authentication.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ApiException(code = ErrorCode.GroupNotFound)
public class GroupNotFoundException extends EntityNotFoundException {


}
