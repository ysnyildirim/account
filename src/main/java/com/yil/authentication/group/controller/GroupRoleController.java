package com.yil.authentication.group.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.group.dto.CreateGroupRoleDto;
import com.yil.authentication.group.dto.GroupRoleDto;
import com.yil.authentication.group.model.GroupRole;
import com.yil.authentication.group.service.GroupRoleService;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account/v1/groups/{groupId}/roles")
public class GroupRoleController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final GroupRoleService groupRoleService;
    private final RoleService roleService;

    @Autowired
    public GroupRoleController(GroupRoleService groupRoleService, RoleService roleService) {
        this.groupRoleService = groupRoleService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<GroupRoleDto>> findAll(@PathVariable Long groupId) {
        List<GroupRole> data = groupRoleService.findAllByGroupIdAndDeletedTimeIsNull(groupId);
        List<GroupRoleDto> dtoData = new ArrayList<>();
        for (GroupRole groupRole : data) {
            GroupRoleDto dto = GroupRoleService.toDto(groupRole);
            dtoData.add(dto);
        }
        return ResponseEntity.ok(dtoData);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupRoleDto> create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long groupId,
                                               @Valid @RequestBody CreateGroupRoleDto request) {
        Role role = roleService.findById(request.getRoleId());
        List<GroupRole> groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, request.getRoleId());
        GroupRoleDto responce;
        if (!groupRoles.isEmpty()) {
            responce = GroupRoleService.toDto(groupRoles.get(0));
            return ResponseEntity.created(null).body(responce);
        }
        GroupRole groupRole = new GroupRole();
        groupRole.setGroupId(groupId);
        groupRole.setRoleId(role.getId());
        groupRole.setCreatedUserId(authenticatedUserId);
        groupRole.setCreatedTime(new Date());
        groupRole = groupRoleService.save(groupRole);
        responce = GroupRoleService.toDto(groupRole);
        return ResponseEntity.created(null).body(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) {
        List<GroupRole> groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, id);
        if (groupRoles.isEmpty())
            return ResponseEntity.ok().build();
        for (GroupRole groupRole : groupRoles) {
            groupRole.setDeletedUserId(authenticatedUserId);
            groupRole.setDeletedTime(new Date());
        }
        groupRoleService.saveAll(groupRoles);
        return ResponseEntity.ok().build();
    }

}
