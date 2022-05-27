/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.authentication.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    GroupNotFound(2000001, "Group not found", HttpStatus.NOT_FOUND),
    PermissionNotFound(2000002, "Permission not found", HttpStatus.NOT_FOUND),
    RoleNameCannotBeUsed(2000003, "Role name cannot be used", HttpStatus.BAD_REQUEST),
    RoleNotFound(2000004, "Role not found", HttpStatus.NOT_FOUND),
    UserNameCannotBeUsed(2000005, "User name cannot be used", HttpStatus.BAD_REQUEST),
    UserNotFound(2000006, "User not found", HttpStatus.NOT_FOUND),
    UserRoleNotFound(2000007, "User role not found", HttpStatus.NOT_FOUND),
    UserTypeNotFound(2000008, "User type not found", HttpStatus.NOT_FOUND),
    GroupNameCannotBeUsed(2000009, "Group name cannot be used", HttpStatus.BAD_REQUEST);

    private int code;

    private String message;

    private HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }


}
