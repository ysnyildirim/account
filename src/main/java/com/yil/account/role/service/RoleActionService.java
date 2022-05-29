package com.yil.account.role.service;

import com.yil.account.role.model.RoleAction;
import com.yil.account.role.repository.RoleActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleActionService {
    private final RoleActionRepository roleActionRepository;

    @Autowired
    public RoleActionService(RoleActionRepository roleActionRepository) {
        this.roleActionRepository = roleActionRepository;
    }

    public RoleAction save(RoleAction roleAction) {
        return roleActionRepository.save(roleAction);
    }

    public List<RoleAction> saveAll(List<RoleAction> roles) {
        return roleActionRepository.saveAll(roles);
    }

    public List<RoleAction> findAllByRoleIdAndActionIdAndDeletedTimeIsNull(Long roleId, Long actionId) {
        return roleActionRepository.findAllByRoleIdAndActionIdAndDeletedTimeIsNull(roleId, actionId);
    }

    public Page<RoleAction> findAllByRoleIdAndDeletedTimeIsNull(Pageable pageable, Long roleId) {
        return roleActionRepository.findAllByRoleIdAndDeletedTimeIsNull(pageable, roleId);
    }
}
