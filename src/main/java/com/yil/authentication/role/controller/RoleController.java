package com.yil.authentication.role.controller;

import com.yil.authentication.base.ApiConstant;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.exception.RoleNameCannotBeUsedException;
import com.yil.authentication.role.dto.CreateRoleDto;
import com.yil.authentication.role.dto.RoleDto;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/account/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<PageDto<RoleDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> rolePage = roleService.findAllByDeletedTimeIsNull(pageable);
        PageDto<RoleDto> pageDto = PageDto.toDto(rolePage, RoleService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable Long id) {
        Role entity = roleService.findById(id);
        RoleDto dto = RoleService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoleDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @Valid @RequestBody CreateRoleDto dto) {
        if (roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        Role entity = new Role();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = roleService.save(entity);
        RoleDto responce = RoleService.toDto(entity);
        return ResponseEntity.created(null).body(responce);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RoleDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CreateRoleDto dto) {
        Role role = roleService.findByIdAndDeletedTimeIsNull(id);
        if (roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role = roleService.save(role);
        RoleDto responce = RoleService.toDto(role);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        Role entity = roleService.findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        roleService.save(entity);
        return ResponseEntity.ok().build();
    }

}
