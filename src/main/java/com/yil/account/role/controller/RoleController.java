package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.RoleNameCannotBeUsedException;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.role.dto.CreateRoleDto;
import com.yil.account.role.dto.CreateRoleResponse;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/acc/v1/roles")
public class RoleController {
    private final RoleService roleService;
    private final Mapper<Role, RoleDto> mapper = new Mapper<>(RoleService::convert);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<RoleDto>> findAll(@PageableDefault Pageable pageable) {
        PageDto<RoleDto> pageDto = mapper.map(roleService.findAll(pageable));
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RoleDto> findById(@PathVariable Integer id) throws RoleNotFoundException {
        RoleDto dto = mapper.map(roleService.findById(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateRoleResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                     @Valid @RequestBody CreateRoleDto dto) throws RoleNameCannotBeUsedException {
        if (roleService.existsByName(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        Role entity = new Role();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAssignable(dto.getAssignable());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedDate(new Date());
        entity = roleService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateRoleResponse.builder().id(entity.getId()).build());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @PathVariable Integer id,
                                          @Valid @RequestBody CreateRoleDto dto) throws RoleNotFoundException, RoleNameCannotBeUsedException {
        Role role = roleService.findById(id);
        if (!role.getName().equals(dto.getName()) && roleService.existsByName(dto.getName()))
            throw new RoleNameCannotBeUsedException();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setAssignable(dto.getAssignable());
        role.setLastModifyDate(new Date());
        role.setLastModifyUserId(authenticatedUserId);
        roleService.save(role);
        return ResponseEntity.ok("Replaced");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        roleService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}
