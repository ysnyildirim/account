package com.yil.account.group.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.exception.GroupRoleNotFound;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.group.dto.CreateGroupRoleDto;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.group.service.GroupService;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final GroupRoleService groupRoleService;
    private final RoleService roleService;
    private final GroupService groupService;

    @Autowired
    public GroupRoleController(GroupRoleService groupRoleService, RoleService roleService, GroupService groupService) {
        this.groupRoleService = groupRoleService;
        this.roleService = roleService;
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<PageDto<RoleDto>> findAll(@PathVariable Long groupId,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws RoleNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupRole> data = groupRoleService.findAllByGroupIdAndDeletedTimeIsNull(pageable, groupId);
        List<RoleDto> dtoData = new ArrayList<>();
        for (GroupRole groupRole : data.getContent()) {
            Role role = roleService.findById(groupRole.getRoleId());
            RoleDto roleDto = RoleService.toDto(role);
            dtoData.add(roleDto);
        }
        PageDto<RoleDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody CreateGroupRoleDto request) throws RoleNotFoundException, GroupNotFoundException {
        Role role = roleService.findById(request.getRoleId());
        Group group = groupService.findByIdAndDeletedTimeIsNull(groupId);
        List<GroupRole> groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(group.getId(), role.getId());
        if (!groupRoles.isEmpty())
            return ResponseEntity.created(null).build();
        GroupRole groupRole = new GroupRole();
        groupRole.setGroupId(group.getId());
        groupRole.setRoleId(role.getId());
        groupRole.setCreatedUserId(authenticatedUserId);
        groupRole.setCreatedTime(new Date());
        groupRole = groupRoleService.save(groupRole);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) throws RoleNotFoundException, GroupNotFoundException, GroupRoleNotFound {
        List<GroupRole> groupRoles = groupRoleService.findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(groupId, id);
        if (groupRoles.isEmpty())
            throw new GroupRoleNotFound();
        for (GroupRole groupRole : groupRoles) {
            groupRole.setDeletedUserId(authenticatedUserId);
            groupRole.setDeletedTime(new Date());
        }
        groupRoleService.saveAll(groupRoles);
        return ResponseEntity.ok().build();
    }

}
