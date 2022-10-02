/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.user.service;

import com.yil.account.user.dao.UserRoleDao;
import com.yil.account.user.dto.UserRoleDto;
import com.yil.account.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleDao userRoleDao;

    public static UserRoleDto toDto(UserRole UserRole) {
        return UserRoleDto
                .builder()
                .roleId(UserRole.getId().getRoleId())
                .userId(UserRole.getId().getUserId())
                .build();
    }

    public UserRole save(UserRole UserRole) {
        return userRoleDao.save(UserRole);
    }

    public List<UserRole> saveAll(List<UserRole> roles) {
        return userRoleDao.saveAll(roles);
    }

    public void delete(UserRole.Pk id) {
        userRoleDao.deleteById(id);
    }

    public List<UserRole> findAllByUserId(Long userId) {
        return userRoleDao.findAllById_UserId(userId);
    }
}
