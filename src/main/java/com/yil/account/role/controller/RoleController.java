package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.RoleNameCannotBeUsedException;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.CreateRoleDto;
import com.yil.account.role.dto.RoleDto;
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
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account/v1/roles")
public class RoleController {

    private final RoleService roleService;
    private final Mapper<Role, RoleDto> mapper = new Mapper<Role, RoleDto>(RoleService::convert);

    @GetMapping
    public ResponseEntity<PageDto<RoleDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> rolePage = roleService.findAll(pageable);
        PageDto<RoleDto> pageDto = mapper.map(roleService.findAll(pageable));
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable Long id) throws RoleNotFoundException {
        RoleDto dto = mapper.map(roleService.findById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoleDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @Valid @RequestBody CreateRoleDto dto) throws RoleNameCannotBeUsedException {
        if (roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        Role entity = new Role();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAssignable(dto.getAssignable());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = roleService.save(entity);
        RoleDto responce = mapper.map(entity);
        return ResponseEntity.created(null).body(responce);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RoleDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CreateRoleDto dto) throws RoleNotFoundException, RoleNameCannotBeUsedException {
        Role role = roleService.findById(id);
        if (!role.getName().equals(dto.getName()) && roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setAssignable(dto.getAssignable());
        role = roleService.save(role);
        RoleDto responce = mapper.map(role);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) throws RoleNotFoundException {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }

}
