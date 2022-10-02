package com.yil.account.group.service;

import com.yil.account.exception.GroupRoleNotFound;
import com.yil.account.group.dto.GroupRoleDto;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.repository.GroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRoleService {
    private final GroupRoleRepository groupRoleRepository;

    public static GroupRoleDto convert(GroupRole groupRole) {
        return GroupRoleDto
                .builder()
                .roleId(groupRole.getId().getRoleId())
                .groupId(groupRole.getId().getGroupId())
                .build();
    }

    public GroupRole save(GroupRole groupRole) {
        return groupRoleRepository.save(groupRole);
    }

    public List<GroupRole> saveAll(List<GroupRole> roles) {
        return groupRoleRepository.saveAll(roles);
    }

    public void delete(GroupRole.Pk id) {
        groupRoleRepository.deleteById(id);
    }

    public GroupRole findById(GroupRole.Pk id) throws GroupRoleNotFound {
        return groupRoleRepository.findById(id).orElseThrow(GroupRoleNotFound::new);
    }

    public boolean existsById(GroupRole.Pk id) {
        return groupRoleRepository.existsById(id);
    }

    public Page<GroupRole> findAllById_GroupId(Pageable pageable, Long groupId) {
        return groupRoleRepository.findAllById_GroupId(pageable, groupId);
    }

    public List<GroupRole> findAllById_GroupId(Long groupId) {
        return groupRoleRepository.findAllById_GroupId(groupId);
    }

    public List<Long> getRoleIdByGroupId(Long groupId) {
        return groupRoleRepository.getRoleIdByGroupId(groupId);
    }
}
