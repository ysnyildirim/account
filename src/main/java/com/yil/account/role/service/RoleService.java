package com.yil.account.role.service;

import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public static RoleDto toDto(Role role) {
        if (role == null)
            throw new NullPointerException();
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .assignable(role.getAssignable())
                .build();
    }

    public Role findById(Long id) throws RoleNotFoundException {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException());
    }

    public Role findByIdAndDeletedTimeIsNull(Long id) throws RoleNotFoundException {
        Role role = roleRepository.findByIdAndDeletedTimeIsNull(id);
        if (role == null)
            throw new RoleNotFoundException();
        return role;
    }

    public Role findByName(String name) throws RoleNotFoundException {
        Role role = roleRepository.findByNameAndDeletedTimeIsNull(name);
        if (role == null)
            throw new RoleNotFoundException();
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
