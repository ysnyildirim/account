package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.RolePermissionDto;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.service.PermissionService;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/roles/{roleId}/permissions")
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final Mapper<RolePermission, RolePermissionDto> mapper = new Mapper<>(RolePermissionService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RolePermissionDto>> findAll(@PathVariable Integer roleId) {
        return ResponseEntity.ok(mapper.map(rolePermissionService.findAllByRoleId(roleId)));
    }

    @PostMapping(value = "/{permissionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(@PathVariable Integer roleId,
                                         @PathVariable Integer permissionId) throws PermissionNotFoundException, RoleNotFoundException {
        if (!roleService.existsById(roleId))
            throw new RoleNotFoundException();
        if (!permissionService.existsById(permissionId))
            throw new PermissionNotFoundException();
        RolePermission.Pk id = RolePermission.Pk.builder().permissionId(permissionId).roleId(roleId).build();
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(id);
        rolePermissionService.save(rolePermission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/{permissionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Integer roleId,
                                         @PathVariable Integer permissionId) {
        RolePermission.Pk pk = RolePermission.Pk.builder().roleId(roleId).permissionId(permissionId).build();
        rolePermissionService.delete(pk);
        return ResponseEntity.ok("Deleted");
    }
}
