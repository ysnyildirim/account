package com.yil.account.role.service;

import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.model.Permission;
import com.yil.account.role.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public static PermissionDto convert(@NotNull Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }

    public Permission findById(Long id) throws PermissionNotFoundException {
        return permissionRepository.findById(id).orElseThrow(() -> new PermissionNotFoundException());
    }

    public Permission findByName(String name) throws PermissionNotFoundException {
        Permission permission = permissionRepository.findByName(name);
        if (permission == null)
            throw new PermissionNotFoundException();
        return permission;
    }

    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }

    public Permission save(Permission user) {
        return permissionRepository.save(user);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    public Page<Permission> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    public boolean existsById(Long id) {
        return permissionRepository.existsById(id);
    }
}
