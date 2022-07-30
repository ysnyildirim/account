package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.dto.PermissionRequest;
import com.yil.account.role.model.Permission;
import com.yil.account.role.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<PageDto<PermissionDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Permission> permissionPage = permissionService.findAll(pageable);
        PageDto<PermissionDto> pageDto = PageDto.toDto(permissionPage, PermissionService::convert);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PermissionDto> findById(@PathVariable Long id) throws PermissionNotFoundException {
        Permission entity = permissionService.findById(id);
        PermissionDto dto = PermissionService.convert(entity);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PermissionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @Valid @RequestBody PermissionRequest dto)  {
        Permission entity = new Permission();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = permissionService.save(entity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        PermissionDto responce = PermissionService.convert(entity);
        return ResponseEntity.created(uri).body(responce);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PermissionDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody PermissionRequest dto) throws PermissionNotFoundException {
        Permission permission = permissionService.findById(id);
        if (permissionService.existsByName(dto.getName()))
            return ResponseEntity.badRequest().build();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permission = permissionService.save(permission);
        PermissionDto responce = PermissionService.convert(permission);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) throws PermissionNotFoundException {
        permissionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
