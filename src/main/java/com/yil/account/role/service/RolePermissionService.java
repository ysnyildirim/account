package com.yil.account.role.service;

import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    public List<RolePermission> saveAll(List<RolePermission> roles) {
        return rolePermissionRepository.saveAll(roles);
    }

    public List<RolePermission> findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(Long roleId, Long permissionId) {
        return rolePermissionRepository.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, permissionId);
    }

    public Page<RolePermission> findAllByRoleIdAndDeletedTimeIsNull(Pageable pageable, Long roleId) {
        return rolePermissionRepository.findAllByRoleIdAndDeletedTimeIsNull(pageable, roleId);
    }
}
