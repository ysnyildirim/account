package com.yil.authentication.role.service;

import com.yil.authentication.role.controller.dto.RoleDto;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public static RoleDto toDto(Role role) {
        if (role == null)
            throw new NullPointerException();
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public Role findById(Long id) throws EntityNotFoundException {
        return roleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Role not found");
        });
    }

    public Role findByName(String name) throws EntityNotFoundException {
        Role role = roleRepository.findByNameAndDeletedTimeIsNull(name);
        if (role == null)
            throw new EntityNotFoundException("Role not found");
        return role;
    }

    public boolean existsByNameAndDeletedTimeIsNull(String name) {
        return roleRepository.existsByNameAndDeletedTimeIsNull(name);
    }

    public Role save(Role user) {
        return roleRepository.save(user);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Page<Role> findAllByDeletedTimeIsNull(Pageable pageable) {
        return roleRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
