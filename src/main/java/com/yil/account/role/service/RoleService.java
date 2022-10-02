package com.yil.account.role.service;

import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.repository.RoleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleDao roleDao;

    public static RoleDto convert(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .assignable(role.isAssignable())
                .build();
    }

    public Role findById(Integer id) throws RoleNotFoundException {
        return roleDao.findById(id).orElseThrow(() -> new RoleNotFoundException());
    }

    public boolean existsById(Integer id) {
        return roleDao.existsById(id);
    }

    public boolean existsByName(String name) {
        return roleDao.existsByName(name);
    }

    public Role save(Role user) {
        return roleDao.save(user);
    }

    public void deleteById(Integer id) {
        roleDao.deleteById(id);
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleDao.findAll(pageable);
    }
}
