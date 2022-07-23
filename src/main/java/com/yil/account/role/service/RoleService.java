package com.yil.account.role.service;

import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public static RoleDto convert(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .assignable(role.isAssignable())
                .inheritable(role.isInheritable())
                .build();
    }

    public Role findById(Long id) throws RoleNotFoundException {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
    }

    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    public Role findByName(String name) throws RoleNotFoundException {
        Role role = roleRepository.findByName(name);
        if (role == null)
            throw new RoleNotFoundException();
        return role;
    }

    public boolean existsByNameAndDeletedTimeIsNull(String name) {
        return roleRepository.existsByName(name);
    }

    public Role save(Role user) {
        return roleRepository.save(user);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
