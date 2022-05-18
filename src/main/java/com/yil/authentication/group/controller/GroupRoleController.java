package com.yil.authentication.group.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.group.controller.dto.CreateGroupRoleDto;
import com.yil.authentication.group.controller.dto.GroupRoleDto;
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

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "v1/groups/{groupId}/roles")
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
        try {
            List<GroupRole> data = groupRoleService.findAllByGroupIdAndDeletedTimeIsNull(groupId);
            List<GroupRoleDto> dtoData = new ArrayList<>();
            for (GroupRole groupRole : data) {
                GroupRoleDto dto = GroupRoleService.toDto(groupRole);
                dtoData.add(dto);
            }
            return ResponseEntity.ok(dtoData);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody CreateGroupRoleDto dto) {
        try {
            Role role = roleService.findById(dto.getRoleId());
            List<GroupRole> groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, dto.getRoleId());
            if (!groupRoles.isEmpty())
                return ResponseEntity.created(null).build();
            GroupRole groupRole = new GroupRole();
            groupRole.setGroupId(groupId);
            groupRole.setRoleId(role.getId());
            groupRole.setCreatedUserId(authenticatedUserId);
            groupRole.setCreatedTime(new Date());
            groupRole = groupRoleService.save(groupRole);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) {
        try {
            List<GroupRole> groupRoles;
            try {
                groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            for (GroupRole groupRole : groupRoles) {
                groupRole.setDeletedUserId(authenticatedUserId);
                groupRole.setDeletedTime(new Date());
            }
            groupRoleService.saveAll(groupRoles);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
