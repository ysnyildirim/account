package com.yil.account.group.service;

import com.yil.account.group.model.GroupRole;
import com.yil.account.group.repository.GroupRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRoleService {
    private final GroupRoleRepository groupRoleRepository;

    @Autowired
    public GroupRoleService(GroupRoleRepository groupRoleRepository) {
        this.groupRoleRepository = groupRoleRepository;
    }

    public GroupRole save(GroupRole groupRole) {
        return groupRoleRepository.save(groupRole);
    }

    public List<GroupRole> saveAll(List<GroupRole> roles) {
        return groupRoleRepository.saveAll(roles);
    }

    public void delete(Long id) {
        groupRoleRepository.deleteById(id);
    }

    public List<GroupRole> findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(Long groupId, Long roleId) {
        return groupRoleRepository.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, roleId);
    }

    public Page<GroupRole> findAllByGroupIdAndDeletedTimeIsNull(Pageable pageable, Long groupId) {
        return groupRoleRepository.findAllByGroupIdAndDeletedTimeIsNull(pageable, groupId);
    }
}
