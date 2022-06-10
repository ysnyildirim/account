package com.yil.account.role.service;

import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.model.Permission;
import com.yil.account.role.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public static PermissionDto toDto(Permission permission) {
        if (permission == null)
            throw new NullPointerException();
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }

    public Permission findById(Long id) throws PermissionNotFoundException {
        return permissionRepository.findById(id).orElseThrow(() -> new PermissionNotFoundException());
    }

    public Permission findByIdAndDeletedTimeIsNull(Long id) throws PermissionNotFoundException {
        Permission permission = permissionRepository.findByIdAndDeletedTimeIsNull(id);
        if (permission == null)
            throw new PermissionNotFoundException();
        return permission;
    }

    public Permission findByName(String name) throws PermissionNotFoundException {
        Permission permission = permissionRepository.findByNameAndDeletedTimeIsNull(name);
        if (permission == null)
            throw new PermissionNotFoundException();
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
