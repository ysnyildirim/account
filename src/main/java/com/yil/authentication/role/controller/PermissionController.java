package com.yil.authentication.role.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.role.controller.dto.CreatePermissionDto;
import com.yil.authentication.role.controller.dto.PermissionDto;
import com.yil.authentication.role.model.Permission;
import com.yil.authentication.role.service.PermissionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("v1/permissions")
public class PermissionController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<PageDto<PermissionDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Permission> permissionPage = permissionService.findAllByDeletedTimeIsNull(pageable);
            PageDto<PermissionDto> pageDto = PageDto.toDto(permissionPage, PermissionService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {

            logger.error(null, exception);
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PermissionDto> findById(@PathVariable Long id) {
        try {
            Objects.requireNonNull(id, "Id is null");
            Permission entity = permissionService.findById(id);
            PermissionDto dto = PermissionService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreatePermissionDto dto) {
        try {
            if (permissionService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            Permission entity = Permission.builder()
                    .name(dto.getName())
                    .build();
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = permissionService.save(entity);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreatePermissionDto dto) {
        try {
            Permission permission;
            try {
                permission = permissionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (permissionService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            permission.setName(dto.getName());
            permission = permissionService.save(permission);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        try {
            Permission entity;
            try {
                entity = permissionService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            permissionService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
