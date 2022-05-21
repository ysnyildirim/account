package com.yil.authentication.group.service;

import com.yil.authentication.group.dto.GroupRoleDto;
import com.yil.authentication.group.model.GroupRole;
import com.yil.authentication.group.repository.GroupRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class GroupRoleService {
    private final GroupRoleRepository groupRoleRepository;

    @Autowired
    public GroupRoleService(GroupRoleRepository groupRoleRepository) {
        this.groupRoleRepository = groupRoleRepository;
    }

    public static GroupRoleDto toDto(GroupRole f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException();
        GroupRoleDto dto = new GroupRoleDto();
        dto.setRoleId(f.getRoleId());
        dto.setGroupId(f.getGroupId());
        return dto;
    }

    public GroupRole findById(Long id) throws EntityNotFoundException {
        return groupRoleRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
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

    public List<GroupRole> findAllByGroupIdAndDeletedTimeIsNull(Long groupId) {
        return groupRoleRepository.findAllByGroupIdAndDeletedTimeIsNull(groupId);
    }

    public List<GroupRole> findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(Long groupId, Long roleId) {
        return groupRoleRepository.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, roleId);
    }

}
