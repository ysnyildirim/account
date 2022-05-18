package com.yil.authentication.role.service;

import com.yil.authentication.role.controller.dto.PermissionDto;
import com.yil.authentication.role.model.Permission;
import com.yil.authentication.role.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public static PermissionDto toDto(Permission permission) {
        if (permission == null)
            throw new NullPointerException();
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }

    public Permission findById(Long id) throws EntityNotFoundException {
        return permissionRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Permission not found");
        });
    }

    public Permission findByName(String name) throws EntityNotFoundException {
        Permission permission = permissionRepository.findByNameAndDeletedTimeIsNull(name);
        if (permission == null)
            throw new EntityNotFoundException("Permission not found");
        return permission;
    }

    public boolean existsByNameAndDeletedTimeIsNull(String name) {
        return permissionRepository.existsByNameAndDeletedTimeIsNull(name);
    }

    public Permission save(Permission user) {
        return permissionRepository.save(user);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    public Page<Permission> findAllByDeletedTimeIsNull(Pageable pageable) {
        return permissionRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
