package com.yil.authentication.role.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.role.controller.dto.CreateRolePermissionDto;
import com.yil.authentication.role.controller.dto.RolePermissionDto;
import com.yil.authentication.role.model.Permission;
import com.yil.authentication.role.model.RolePermission;
import com.yil.authentication.role.service.PermissionService;
import com.yil.authentication.role.service.RolePermissionService;
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
@RequestMapping(value = "v1/roles/{roleId}/permissions")
public class RolePermissionController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<RolePermissionDto>> findAll(@PathVariable Long roleId) {
        try {
            List<RolePermission> data = rolePermissionService.findAllByRoleIdAndDeletedTimeIsNull(roleId);
            List<RolePermissionDto> dtoData = new ArrayList<>();
            for (RolePermission rolePermission : data) {
                RolePermissionDto dto = RolePermissionService.toDto(rolePermission);
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
                                 @PathVariable Long roleId,
                                 @Valid @RequestBody CreateRolePermissionDto dto) {
        try {
            Permission permission = permissionService.findById(dto.getPermissionId());
            List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, dto.getPermissionId());
            if (!rolePermissions.isEmpty())
                return ResponseEntity.created(null).build();
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permission.getId());
            rolePermission.setCreatedUserId(authenticatedUserId);
            rolePermission.setCreatedTime(new Date());
            rolePermission = rolePermissionService.save(rolePermission);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @PathVariable Long id) {
        try {
            List<RolePermission> rolePermissions;
            try {
                rolePermissions = rolePermissionService.findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(roleId, id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            for (RolePermission rolePermission : rolePermissions) {
                rolePermission.setDeletedUserId(authenticatedUserId);
                rolePermission.setDeletedTime(new Date());
            }
            rolePermissionService.saveAll(rolePermissions);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
