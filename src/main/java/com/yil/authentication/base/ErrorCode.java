/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.authentication.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    GroupNotFound(2000001, "Group not found"),
    PermissionNotFound(2000002, "Permission not found"),
    RoleNameCannotBeUsed(2000003, "Role name cannot be used"),
    RoleNotFound(2000004, "Role not found"),
    UserNameCannotBeUsed(2000005, "User name cannot be used"),
    UserNotFound(2000006, "User not found"),
    UserRoleNotFound(2000007, "User role not found"),
    UserTypeNotFound(2000008, "User type not found"),
    GroupNameCannotBeUsed(2000009, "Group name cannot be used");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
