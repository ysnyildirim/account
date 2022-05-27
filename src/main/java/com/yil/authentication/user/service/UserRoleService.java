package com.yil.authentication.user.service;

import com.yil.authentication.user.dto.UserRoleDto;
import com.yil.authentication.user.model.UserRole;
import com.yil.authentication.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public static UserRoleDto toDto(UserRole f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException();
        UserRoleDto dto = new UserRoleDto();
        dto.setRoleId(f.getRoleId());
        dto.setUserId(f.getUserId());
        return dto;
    }

    public UserRole findById(Long id) throws EntityNotFoundException {
        return userRoleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
    }

    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public List<UserRole> saveAll(List<UserRole> roles) {
        return userRoleRepository.saveAll(roles);
    }

    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }

    public List<UserRole> findAllByUserIdAndDeletedTimeIsNull(Long userId) {
        return userRoleRepository.findAllByUserIdAndDeletedTimeIsNull(userId);
    }

    public List<UserRole> findAllByUserIdAndRoleIdAndDeletedTimeIsNull(Long userId, Long roleId) {
        return userRoleRepository.findAllByUserIdAndRoleIdAndDeletedTimeIsNull(userId, roleId);
    }

}
