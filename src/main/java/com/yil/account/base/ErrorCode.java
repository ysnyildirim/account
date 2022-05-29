/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RolePermissionNotFound(2000015, "Role permission not found"),
    GroupRoleNotFound(2000014, "Group role not found"),
    GroupUserNotFound(2000013, "Group user not found"),
    WrongPassword(2000012, "Wrong password"),
    LockedUser(2000011, "User locked"),
    DisabledUser(2000010, "User disabled"),
    GroupNameCannotBeUsed(2000009, "Group name cannot be used"),
    UserTypeNotFound(2000008, "User type not found"),
    UserRoleNotFound(2000007, "User role not found"),
    UserNotFound(2000006, "User not found"),
    UserNameCannotBeUsed(2000005, "User name cannot be used"),
    RoleNotFound(2000004, "Role not found"),
    RoleNameCannotBeUsed(2000003, "Role name cannot be used"),
    PermissionNotFound(2000002, "Permission not found"),
    GroupNotFound(2000001, "Group not found");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
