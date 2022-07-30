package com.yil.account.group.service;

import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.group.dto.GroupDto;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.repository.GroupRepository;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupRoleService groupRoleService;
    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;

    public static GroupDto toDto(Group group) {
        if (group == null)
            throw new NullPointerException();
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .build();
    }

    public Group findById(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException());
    }

    public boolean existsById(Long id) {
        return groupRepository.existsById(id);
    }

    public Group findByIdAndDeletedTimeIsNull(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }

    public Group findByName(String name) throws GroupNotFoundException {
        Group group = groupRepository.findByName(name);
        if (group == null)
            throw new GroupNotFoundException();
        return group;
    }

    public boolean existsByNameAndDeletedTimeIsNull(String name) {
        return groupRepository.existsByName(name);
    }

    public Group save(Group user) {
        return groupRepository.save(user);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public boolean existsGroupPermission(long groupId, long permissionId) {
        List<GroupRole> groupRoles = groupRoleService.findAllById_GroupId(groupId);
        for (GroupRole groupRole : groupRoles) {
            if (rolePermissionService.existsById(RolePermission.Pk.builder().roleId(groupRole.getId().getRoleId()).permissionId(permissionId).build()))
                return true;
        }
        return false;
    }

}
