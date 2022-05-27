package com.yil.authentication.role.service;

import com.yil.authentication.role.dto.RolePermissionDto;
import com.yil.authentication.role.model.RolePermission;
import com.yil.authentication.role.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;

    @Autowired
    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public static RolePermissionDto toDto(RolePermission f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException();
        RolePermissionDto dto = new RolePermissionDto();
        dto.setRoleId(f.getRoleId());
        dto.setPermissionId(f.getPermissionId());
        return dto;
    }

    public RolePermission findById(Long id) throws EntityNotFoundException {
        return rolePermissionRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
    }

    public RolePermission save(RolePermission rolePermission) {
        return rolePermissionRepository.save(rolePermission);
    }

    public List<RolePermission> saveAll(List<RolePermission> roles) {
        return rolePermissionRepository.saveAll(roles);
    }

    public void delete(Long id) {
        rolePermissionRepository.deleteById(id);
    }

    public List<RolePermission> findAllByRoleIdAndDeletedTimeIsNull(Long roleId) {
        return rolePermissionRepository.findAllByRoleIdAndDeletedTimeIsNull(roleId);
    }

    public List<RolePermission> findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(Long roleId, Long permissionId) {
        return rolePermissionRepository.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, permissionId);
    }

}
