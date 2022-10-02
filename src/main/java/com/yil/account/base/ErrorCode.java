/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    JwtExpired(2000020, "Token expired"),
    UserPhotoNotFound(2000018, "User photo not found"),
    RolePermissionNotFound(2000015, "Role permission not found"),
    WrongPassword(2000012, "Wrong password"),
    LockedUser(2000011, "User locked"),
    DisabledUser(2000010, "User disabled"),
    UserTypeNotFound(2000008, "User type not found"),
    UserNotFound(2000006, "User not found"),
    UserNameCannotBeUsed(2000005, "User name cannot be used"),
    RoleNotFound(2000004, "Role not found"),
    RoleNameCannotBeUsed(2000003, "Role name cannot be used"),
    PermissionNotFound(2000002, "Permission not found");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
