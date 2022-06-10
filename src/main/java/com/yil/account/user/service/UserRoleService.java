package com.yil.account.user.service;

import com.yil.account.user.model.UserRole;
import com.yil.account.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

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

    public Page<UserRole> findAllByUserIdAndDeletedTimeIsNull(Pageable pageable, Long userId) {
        return userRoleRepository.findAllByUserIdAndDeletedTimeIsNull(pageable, userId);
    }

    public List<UserRole> findAllByUserIdAndRoleIdAndDeletedTimeIsNull(Long userId, Long roleId) {
        return userRoleRepository.findAllByUserIdAndRoleIdAndDeletedTimeIsNull(userId, roleId);
    }

}
