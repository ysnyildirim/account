package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.CreateRolePermissionDto;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.model.Permission;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.service.PermissionService;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/roles/{roleId}/permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<PageDto<PermissionDto>> findAll(@PathVariable Long roleId,
                                                          @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                          @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws PermissionNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<RolePermission> data = rolePermissionService.findAllById_RoleId(pageable, roleId);
        List<PermissionDto> dtoData = new ArrayList<>();
        for (RolePermission rolePermission : data.getContent()) {
            Permission permission = permissionService.findById(rolePermission.getId().getPermissionId());
            PermissionDto permissionDto = PermissionService.convert(permission);
            dtoData.add(permissionDto);
        }
        PageDto<PermissionDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @Valid @RequestBody CreateRolePermissionDto dto) throws PermissionNotFoundException, RoleNotFoundException {
        if (!roleService.existsById(roleId))
            throw new RoleNotFoundException();
        if (!permissionService.existsById(dto.getPermissionId()))
            throw new PermissionNotFoundException();
        RolePermission.Pk id = RolePermission.Pk.builder().permissionId(dto.getPermissionId()).roleId(roleId).build();
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(id);
        rolePermissionService.save(rolePermission);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @PathVariable Long id) {
        RolePermission.Pk pk = RolePermission.Pk.builder().roleId(roleId).permissionId(id).build();
        rolePermissionService.delete(pk);
        return ResponseEntity.ok().build();
    }

}
