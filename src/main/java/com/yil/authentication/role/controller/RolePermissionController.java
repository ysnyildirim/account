package com.yil.authentication.role.controller;

import com.yil.authentication.base.ApiConstant;
import com.yil.authentication.role.dto.CreateRolePermissionDto;
import com.yil.authentication.role.dto.RolePermissionDto;
import com.yil.authentication.role.model.Permission;
import com.yil.authentication.role.model.RolePermission;
import com.yil.authentication.role.service.PermissionService;
import com.yil.authentication.role.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account/v1/roles/{roleId}/permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<RolePermissionDto>> findAll(@PathVariable Long roleId) {
        List<RolePermission> data = rolePermissionService.findAllByRoleIdAndDeletedTimeIsNull(roleId);
        List<RolePermissionDto> dtoData = new ArrayList<>();
        for (RolePermission rolePermission : data) {
            RolePermissionDto dto = RolePermissionService.toDto(rolePermission);
            dtoData.add(dto);
        }
        return ResponseEntity.ok(dtoData);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RolePermissionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                    @PathVariable Long roleId,
                                                    @Valid @RequestBody CreateRolePermissionDto dto) {
        Permission permission = permissionService.findById(dto.getPermissionId());
        List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, dto.getPermissionId());
        RolePermissionDto responce = null;
        if (!rolePermissions.isEmpty()) {
            responce = RolePermissionService.toDto(rolePermissions.get(0));
            return ResponseEntity.created(null).body(responce);
        }
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permission.getId());
        rolePermission.setCreatedUserId(authenticatedUserId);
        rolePermission.setCreatedTime(new Date());
        rolePermission = rolePermissionService.save(rolePermission);
        responce = RolePermissionService.toDto(rolePermission);
        return ResponseEntity.created(null).body(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @PathVariable Long id) {
        List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, id);
        if (rolePermissions.isEmpty())
            return ResponseEntity.ok().build();
        for (RolePermission rolePermission : rolePermissions) {
            rolePermission.setDeletedUserId(authenticatedUserId);
            rolePermission.setDeletedTime(new Date());
        }
        rolePermissionService.saveAll(rolePermissions);
        return ResponseEntity.ok().build();
    }

}
