package com.yil.account.user.service;

import com.yil.account.exception.UserRoleNotFound;
import com.yil.account.user.model.UserRole;
import com.yil.account.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public List<UserRole> saveAll(List<UserRole> roles) {
        return userRoleRepository.saveAll(roles);
    }

    public void deleteById(UserRole.Pk id) {
        userRoleRepository.deleteById(id);
    }

    public void delete(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    public Page<UserRole> findAllById_UserId(Pageable pageable, Long userId) {
        return userRoleRepository.findAllById_UserId(pageable, userId);
    }

    public List<UserRole> findAllById_UserId(Long userId) {
        return userRoleRepository.findAllById_UserId(userId);
    }

    public UserRole findById(UserRole.Pk id) throws UserRoleNotFound {
        return userRoleRepository.findById(id).orElseThrow(UserRoleNotFound::new);
    }

    public boolean existsById(UserRole.Pk id) {
        return userRoleRepository.existsById(id);
    }

}
