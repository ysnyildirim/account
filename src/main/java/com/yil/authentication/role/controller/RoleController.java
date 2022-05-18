package com.yil.authentication.role.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.role.controller.dto.CreateRoleDto;
import com.yil.authentication.role.controller.dto.RoleDto;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.service.RoleService;
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
@RequestMapping("v1/roles")
public class RoleController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<PageDto<RoleDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Role> rolePage = roleService.findAllByDeletedTimeIsNull(pageable);
            PageDto<RoleDto> pageDto = PageDto.toDto(rolePage, RoleService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {

            logger.error(null, exception);
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable Long id) {
        try {
            Objects.requireNonNull(id, "Id is null");
            Role entity = roleService.findById(id);
            RoleDto dto = RoleService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateRoleDto dto) {
        try {
            if (dto.getName() == null || dto.getDescription() == null)
                return ResponseEntity.badRequest().build();
            if (roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            Role entity = Role.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .build();
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = roleService.save(entity);
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
                                  @Valid @RequestBody CreateRoleDto dto) {
        try {
            if (dto.getName() == null || dto.getDescription() == null)
                return ResponseEntity.badRequest().build();
            Role role;
            try {
                role = roleService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (roleService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            role.setName(dto.getName());
            role.setDescription(dto.getDescription());
            role = roleService.save(role);
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
            Role entity;
            try {
                entity = roleService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            roleService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
