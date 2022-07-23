package com.yil.account.group.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.exception.GroupRoleNotFound;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.group.dto.CreateGroupRoleDto;
import com.yil.account.group.dto.GroupRoleDto;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.group.service.GroupService;
import com.yil.account.role.model.Role;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/groups/{groupId}/roles")
public class GroupRoleController {

    private final GroupRoleService groupRoleService;
    private final RoleService roleService;
    private final GroupService groupService;

    private final Mapper<GroupRole, GroupRoleDto> mapper = new Mapper<>(GroupRoleService::convert);

    @GetMapping
    public ResponseEntity<PageDto<GroupRoleDto>> findAll(@PathVariable Long groupId,
                                                         @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                         @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws RoleNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupRole> data = groupRoleService.findAllById_GroupId(pageable, groupId);
        return ResponseEntity.ok(mapper.map(data));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody CreateGroupRoleDto request) throws RoleNotFoundException, GroupNotFoundException {
        Role role = roleService.findById(request.getRoleId());
        Group group = groupService.findByIdAndDeletedTimeIsNull(groupId);
        if (!groupRoleService.existsById(GroupRole.Pk.builder().groupId(groupId).roleId(request.getRoleId()).build())) {
            GroupRole groupRole = new GroupRole();
            groupRole.setId(GroupRole.Pk.builder().groupId(groupId).roleId(request.getRoleId()).build());
            groupRole = groupRoleService.save(groupRole);
        }
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) throws RoleNotFoundException, GroupNotFoundException, GroupRoleNotFound {
        groupRoleService.delete(GroupRole.Pk.builder().groupId(groupId).roleId(id).build());
        return ResponseEntity.ok().build();
    }

}
