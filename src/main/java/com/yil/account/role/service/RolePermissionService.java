package com.yil.account.role.service;

import com.yil.account.role.dto.RolePermissionDto;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.RolePermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    private final RolePermissionDao rolePermissionDao;

    public static RolePermissionDto toDto(RolePermission rolePermission) {
        return RolePermissionDto.builder()
                .permissionId(rolePermission.getId().getPermissionId())
                .roleId(rolePermission.getId().getRoleId())
                .build();
    }

    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionDao.save(rolePermission);
    }

    public List<RolePermission> findAllByRoleId(Integer roleId) {
        return rolePermissionDao.findAllById_RoleId(roleId);
    }


    public void delete(RolePermission.Pk id) {
        rolePermissionDao.deleteById(id);
    }

    public boolean existsById(RolePermission.Pk id) {
        return rolePermissionDao.existsById(id);
    }
}
