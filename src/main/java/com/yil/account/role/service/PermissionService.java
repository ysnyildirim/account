package com.yil.account.role.service;

import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.model.Permission;
import com.yil.account.role.repository.PermissionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionDao permissionDao;

    public static PermissionDto convert(@NotNull Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }

    public Permission findById(Long id) throws PermissionNotFoundException {
        return permissionDao.findById(id).orElseThrow(() -> new PermissionNotFoundException());
    }

    public Permission findByName(String name) throws PermissionNotFoundException {
        Permission permission = permissionDao.findByName(name);
        if (permission == null)
            throw new PermissionNotFoundException();
        return permission;
    }

    public boolean existsByName(String name) {
        return permissionDao.existsByName(name);
    }

    public Permission save(Permission user) {
        return permissionDao.save(user);
    }

    public void delete(Long id) {
        permissionDao.deleteById(id);
    }

    public Page<Permission> findAll(Pageable pageable) {
        return permissionDao.findAll(pageable);
    }

    public boolean existsById(Long id) {
        return permissionDao.existsById(id);
    }
}
