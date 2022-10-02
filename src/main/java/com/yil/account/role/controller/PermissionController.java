package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.PermissionNotFoundException;
import com.yil.account.role.dto.CreatePermissionDto;
import com.yil.account.role.dto.PermissionDto;
import com.yil.account.role.dto.PermissionRequest;
import com.yil.account.role.model.Permission;
import com.yil.account.role.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final Mapper<Permission, PermissionDto> mapper = new Mapper<>(PermissionService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<PermissionDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(mapper.map(permissionService.findAll(pageable)));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PermissionDto> findById(@PathVariable Integer id) throws PermissionNotFoundException {
        Permission entity = permissionService.findById(id);
        PermissionDto dto = PermissionService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreatePermissionDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                      @Valid @RequestBody PermissionRequest dto) {
        Permission entity = new Permission();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedDate(new Date());
        entity = permissionService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatePermissionDto.builder().id(entity.getId()).build());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PermissionDto> replace(@PathVariable Integer id,
                                                 @Valid @RequestBody PermissionRequest dto) throws PermissionNotFoundException {
        Permission permission = permissionService.findById(id);
        if (permissionService.existsByName(dto.getName()))
            return ResponseEntity.badRequest().build();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permission = permissionService.save(permission);
        PermissionDto responce = PermissionService.toDto(permission);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        permissionService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

}
