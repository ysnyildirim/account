package com.yil.account.role.service;

import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.exception.RolePermissionNotFound;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    private final RoleService roleService;
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    public RolePermission findById(RolePermission.Pk id) throws RolePermissionNotFound {
        return rolePermissionRepository.findById(id).orElseThrow(RolePermissionNotFound::new);
    }

    public Page<RolePermission> findAllById_RoleId(Pageable pageable, Long roleId) {
        return rolePermissionRepository.findAllById_RoleId(pageable, roleId);
    }

    public void delete(RolePermission.Pk id) {
        rolePermissionRepository.deleteById(id);
    }

    public boolean existsById(RolePermission.Pk id) {
        return rolePermissionRepository.existsById(id);
    }

}
